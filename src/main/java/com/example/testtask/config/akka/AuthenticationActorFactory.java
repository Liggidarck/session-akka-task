package com.example.testtask.config.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.testtask.actors.AuthenticationActor;
import com.example.testtask.service.AuthenticationService;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationActorFactory {

    private final ActorSystem actorSystem;
    private final AuthenticationService authenticationService;

    public AuthenticationActorFactory(ActorSystem actorSystem, AuthenticationService authenticationService) {
        this.actorSystem = actorSystem;
        this.authenticationService = authenticationService;
    }

    public ActorRef createAuthenticationActor() {
        return actorSystem.actorOf(Props.create(AuthenticationActor.class, authenticationService));
    }
}
