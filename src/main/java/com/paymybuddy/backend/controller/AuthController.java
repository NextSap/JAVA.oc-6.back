package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.request.LoginRequest;
import com.paymybuddy.backend.object.request.SigninRequest;
import com.paymybuddy.backend.object.response.LoginResponse;
import com.paymybuddy.backend.object.response.UserResponse;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        logger.info("User `" + loginRequest.getEmail() + "` logged in");
        return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest signinRequest) {
        UserResponse userResponse = userService.createUser(signinRequest);
        logger.info("User `" + signinRequest.getEmail() + "` created");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
