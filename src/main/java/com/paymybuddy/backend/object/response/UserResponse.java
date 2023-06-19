package com.paymybuddy.backend.object.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class UserResponse {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> contact;
    private BigDecimal balance;
}