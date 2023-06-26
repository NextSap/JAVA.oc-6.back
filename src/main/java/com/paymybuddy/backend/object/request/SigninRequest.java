package com.paymybuddy.backend.object.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class SigninRequest {
    @NotNull(message = "email:Null") @NotNull(message = "email:Required")
    private String email;
    @NotNull(message = "password:Null") @NotNull(message = "password:Required")
    private String password;
    @NotNull(message = "firstName:Null") @NotNull(message = "firstName:Required")
    private String firstName;
    @NotNull(message = "lastName:Null") @NotNull(message = "lastName:Required")
    private String lastName;
}
