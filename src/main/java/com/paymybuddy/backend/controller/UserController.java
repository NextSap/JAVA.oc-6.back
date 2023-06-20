package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestParam String email, @RequestHeader(value = "Authorization", required = false) String token) {
        UserResponse userResponse = userService.getUser(email, token);
        logger.info("Successful request GET /user?email={}", email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        logger.info("Successful request POST /user");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @RequestHeader(value = "Authorization", required = false) String token) {
        UserResponse userResponse = userService.updateUser(userRequest, token);
        logger.info("Successful request PUT /user");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader(value = "Authorization", required = false) String token) {
        userService.deleteUser(token);
        logger.info("Successful request DELETE /user");
    }
}
