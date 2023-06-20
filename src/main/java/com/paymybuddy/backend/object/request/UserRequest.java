package com.paymybuddy.backend.object.request;

import jakarta.validation.constraints.NotNull;
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
public class UserRequest {
    @NotNull(message = "email:Null") @NotNull(message = "email:Required")
    private String email;
    @NotNull(message = "password:Null") @NotNull(message = "password:Required")
    private String password;
    @NotNull(message = "firstName:Null") @NotNull(message = "firstName:Required")
    private String firstName;
    @NotNull(message = "lastName:Null") @NotNull(message = "lastName:Required")
    private String lastName;
    @NotNull(message = "contacts:Null") @NotNull(message = "contacts:Required")
    private List<String> contacts;
    @NotNull(message = "balance:Null") @NotNull(message = "balance:Required")
    private BigDecimal balance;
}
