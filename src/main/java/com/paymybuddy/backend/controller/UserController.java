package com.paymybuddy.backend.controller;

import com.paymybuddy.backend.object.TransferType;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User management", description = "User management")
@RequestMapping(value = "/user")
public class UserController {

    private final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{email}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        UserResponse userResponse = userService.getMinimizedUserResponseByEmail(email);
        logger.info("Successful request GET /user/{}", email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponse> getUser() {
        UserResponse userResponse = userService.getUserResponseByToken();
        logger.info("Successful request GET /user?email={}", userResponse.getEmail());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/add-contact")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponse> addContact(@RequestParam String email) {
        UserResponse userResponse = userService.addContact(email);
        logger.info("Successful request GET /user/add-contact?{}", email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/transfer-money")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponse> transferMoney(@RequestParam TransferType transferType, @RequestParam Double amount) {
        UserResponse userResponse = userService.transferMoney(transferType, amount);
        logger.info("Successful request POST /user?transferType={}&amount={}", transferType, amount);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateUser(userRequest);
        logger.info("Successful request PUT /user");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteUser() {
        userService.deleteUser();
        logger.info("Successful request DELETE /user");
    }
}
