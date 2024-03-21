package com.example.testtask.config.akka;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {
    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("userActor");
    }}
