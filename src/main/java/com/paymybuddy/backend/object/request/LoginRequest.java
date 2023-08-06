package com.paymybuddy.backend.object.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class LoginRequest {
    @NotNull(message = "email:Null") @NotBlank(message = "email:Required")
    private String email;
    @NotNull(message = "password:Null") @NotBlank(message = "password:Required")
    private String password;
    private boolean rememberMe;
}
