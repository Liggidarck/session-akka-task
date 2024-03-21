package com.example.testtask.actors;

import akka.actor.AbstractActor;
import com.example.testtask.dto.HandleLoginDto;
import com.example.testtask.dto.LoginDto;
import com.example.testtask.dto.ErrorMessage;
import com.example.testtask.dto.RegisterDto;
import com.example.testtask.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthenticationActor extends AbstractActor {

    private final AuthenticationService authenticationService;

    public AuthenticationActor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RegisterDto.class, this::handleRegisterRequest)
                .match(HandleLoginDto.class,this::handleLoginRequest)
                .build();
    }

    private void handleLoginRequest(HandleLoginDto handleLoginDto) {
        LoginDto loginDto = handleLoginDto.getLoginDto();
        HttpServletRequest request = handleLoginDto.getRequest();
        HttpServletResponse response = handleLoginDto.getResponse();

        String status = authenticationService.login(loginDto, request, response);

        getSender().tell(status, getSelf());
    }

    private void handleRegisterRequest(RegisterDto request) {
        ErrorMessage isRegistered = authenticationService.registerUser(request);

        ResponseEntity<?> response;
        if (isRegistered.getError().equals("session.errors.emailAlreadyRegistered")) {
            response = new ResponseEntity<>(isRegistered, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            response = new ResponseEntity<>(HttpStatus.OK);
        }
        getSender().tell(response, getSelf());
    }

}
