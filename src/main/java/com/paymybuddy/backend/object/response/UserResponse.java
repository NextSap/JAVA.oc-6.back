package com.paymybuddy.backend.object.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class UserResponse {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> contacts;
    private BigDecimal balance;
}