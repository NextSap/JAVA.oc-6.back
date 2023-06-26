package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.request.LoginRequest;
import com.paymybuddy.backend.object.response.LoginResponse;
import com.paymybuddy.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthControllerTest {

    @MockBean
    private UserService userService;

    @Test
    public void testLogin() {
        when(userService.login(any(LoginRequest.class))).thenReturn(LoginResponse.builder().token("token").build());

        assertEquals("token", userService.login(LoginRequest.builder().build()).getToken());
    }
}
