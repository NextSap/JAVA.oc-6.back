package com.paymybuddy.backend.object.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class UserModel {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> contact;
    private BigDecimal balance;
}
