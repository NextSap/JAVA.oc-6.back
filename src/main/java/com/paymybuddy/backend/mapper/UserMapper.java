package com.paymybuddy.backend.mapper;

import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.SigninRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;

public class UserMapper {

    private static final UserMapper INSTANCE = new UserMapper();

    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserMapper() {
    }

    public UserResponse toUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .contacts(userEntity.getContacts())
                .balance(userEntity.getBalance())
                .build();
    }

    public UserResponse toMinimizedUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }

    public UserEntity toUserEntity(SigninRequest signinRequest, boolean hashPassword) {
        String password = signinRequest.getPassword();
        return UserEntity.builder()
                .email(signinRequest.getEmail())
                .password(hashPassword ? bCryptPasswordEncoder().encode(password) : password)
                .firstName(signinRequest.getFirstName())
                .lastName(signinRequest.getLastName())
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(0.0))
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
