package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.request.LoginRequest;
import com.paymybuddy.backend.object.request.SigninRequest;
import com.paymybuddy.backend.object.response.LoginResponse;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private final String endpoint = "/auth";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLogin() throws Exception {
        when(userService.login(any(LoginRequest.class))).thenReturn(LoginResponse.builder()
                .token("token")
                .build());
        mockMvc.perform(post(endpoint + "/login")
                        .contentType("application/json")
                        .content("{\"email\":\"email\",\"password\":\"password\",\"rememberMe\":false}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"token\":\"token\"}"));
    }

    @Test
    public void testSignIn() throws Exception {
        when(userService.createUser(any(SigninRequest.class))).thenReturn(UserResponse.builder()
                .email("email")
                .firstName("Foo")
                .lastName("Bar")
                .balance(BigDecimal.valueOf(0))
                .contacts(new ArrayList<>())
                .build());
        mockMvc.perform(post(endpoint + "/signin")
                        .contentType("application/json")
                        .content("{\"email\":\"email\",\"password\":\"password\",\"firstName\":\"Foo\",\"lastName\":\"Bar\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"email\":\"email\",\"firstName\":\"Foo\",\"lastName\":\"Bar\",\"contacts\":[],\"balance\":0.0}"));
    }
}
