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

    @GetMapping(value = "/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email, @RequestHeader("Authorization") String token) {
        UserResponse userResponse = userService.getMinimizedUserResponseByEmail(email);
        logger.info("Successful request GET /user/{}", email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String token) {
        UserResponse userResponse = userService.getUserResponseByToken();
        logger.info("Successful request GET /user?email={}", userResponse.getEmail());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/add-contact")
    public ResponseEntity<UserResponse> addContact(@RequestBody String email, @RequestHeader("Authorization") String token) {
        UserResponse userResponse = userService.addContact(email);
        logger.info("Successful request GET /user/add-contact/{}", email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest, @RequestHeader("Authorization") String token) {
        UserResponse userResponse = userService.updateUser(userRequest);
        logger.info("Successful request PUT /user");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("Authorization") String token) {
        userService.deleteUser();
        logger.info("Successful request DELETE /user");
    }
}
