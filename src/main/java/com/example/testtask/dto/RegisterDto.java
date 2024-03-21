package com.example.testtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import javax.json.Json;
import javax.json.JsonObject;


public record RegisterDto(@JsonProperty("email") String email, @JsonProperty("name") String name, @JsonProperty("password") String password) {
    public JsonObject convertToJSON() {
        return Json.createObjectBuilder()
                .add("email", email())
                .add("name", name())
                .add("password", password())
                .build();
    }
}

