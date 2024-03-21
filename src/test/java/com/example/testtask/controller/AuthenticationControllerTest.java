package com.example.testtask.controller;

import com.example.testtask.dto.LoginDto;
import com.example.testtask.dto.RegisterDto;
import com.example.testtask.repository.UserRepository;
import com.example.testtask.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Value(value = "${tests.email.defaultUser}")
    private String USER_EMAIL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.authService.registerUser(new RegisterDto(USER_EMAIL, "test","password"));
    }

    @AfterEach
    void after() {
        this.userRepository.deleteAll();
    }
    @Test
    public void testRegistrationAndLogin() throws Exception {

        // Логин с зарегистрированными учетными данными
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new LoginDto(USER_EMAIL, "password").convertToJSON().toString()))
                .andExpect(status().isOk());

        // Попытка логина с незарегистрированными учетными данными
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new LoginDto("invaliduser@test.com", "invalidpassword").convertToJSON().toString()))
                .andExpect(content().string(""));
    }

    @Test
    public void testTryRegisterAlreadyCreatedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new LoginDto("test@test.com", "password").convertToJSON().toString()))
                .andExpect(status().isOk());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new LoginDto("test@test.com", "password").convertToJSON().toString()))
                .andExpect(content().string(""));
    }

    @Test
    public void tryGetUnAuthorizeData() throws Exception {
        // Попытка получить данные пользователя без логина
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }


}
