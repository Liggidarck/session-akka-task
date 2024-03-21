package com.example.testtask.controller;


import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import com.example.testtask.config.akka.AuthenticationActorFactory;
import com.example.testtask.dto.HandleLoginDto;
import com.example.testtask.dto.LoginDto;
import com.example.testtask.dto.RegisterDto;
import com.example.testtask.model.User;
import com.example.testtask.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationActorFactory authenticationActorFactory;

    public AuthenticationController(AuthenticationActorFactory authenticationActorFactory) {
        this.authenticationActorFactory = authenticationActorFactory;
    }

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public CompletionStage<?> register(@Valid @RequestBody RegisterDto registerDto) {
        ActorRef authenticationActor = authenticationActorFactory.createAuthenticationActor();
        return PatternsCS.ask(authenticationActor, registerDto, Duration.ofMillis(100));
    }

    @PostMapping(path = "/login")
    public CompletionStage<?> loginEmployee(@Valid @RequestBody LoginDto loginDTO,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        HandleLoginDto handleLoginDto = new HandleLoginDto(loginDTO, request, response);

        ActorRef authenticationActor = authenticationActorFactory.createAuthenticationActor();
        return PatternsCS.ask(authenticationActor, handleLoginDto, Duration.ofMillis(100));
    }

    @GetMapping(path = "/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        String principal = authentication.getName();
        User user = userService.getUserByPrincipal(principal);
        return new ResponseEntity<>(user, OK);
    }

}
