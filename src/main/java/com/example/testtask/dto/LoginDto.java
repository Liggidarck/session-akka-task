package com.example.testtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.json.Json;
import javax.json.JsonObject;


public record LoginDto(@JsonProperty("email") String email, @JsonProperty("password") String password) {
    public JsonObject convertToJSON() {
        return Json.createObjectBuilder()
                .add("email", email())
                .add("password", password())
                .build();
    }
}
