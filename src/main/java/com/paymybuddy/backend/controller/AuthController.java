package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.request.AuthRequest;
import com.paymybuddy.backend.object.response.AuthResponse;
import com.paymybuddy.backend.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final Logger logger = LogManager.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = userService.login(authRequest.getEmail(), authRequest.getPassword(), authRequest.isRememberMe());
        logger.info("Successful request POST /auth");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
