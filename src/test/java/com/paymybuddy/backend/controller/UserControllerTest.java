package com.paymybuddy.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String endpoint = "/user";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        userResponse = UserResponse.builder()
                .email("email")
                .firstName("Foo")
                .lastName("Bar")
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(0.0))
                .build();
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.getUserResponseByToken()).thenReturn(userResponse);

        mockMvc.perform(get(endpoint).param("email", "email").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(userResponse)));
    }
}