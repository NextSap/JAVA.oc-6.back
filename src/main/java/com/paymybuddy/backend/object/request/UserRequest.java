package com.paymybuddy.backend.object.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class UserRequest {
    @NotNull(message = "email:Null") @NotBlank(message = "email:Required")
    private String email;
    @NotNull(message = "password:Null") @NotBlank(message = "password:Required")
    private String password;
    @NotNull(message = "firstName:Null") @NotBlank(message = "firstName:Required")
    private String firstName;
    @NotNull(message = "lastName:Null") @NotBlank(message = "lastName:Required")
    private String lastName;
    @NotNull(message = "contacts:Null") @NotBlank(message = "contacts:Required")
    private List<String> contacts;
    @NotNull(message = "balance:Required")
    private Double balance;
}
