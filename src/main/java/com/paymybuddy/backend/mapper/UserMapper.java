package com.paymybuddy.backend.mapper;

import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.util.CredentialUtils;

public class UserMapper {

    private static final UserMapper INSTANCE = new UserMapper();
    private final CredentialUtils credentialUtils = CredentialUtils.getInstance();

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

    public UserEntity toUserEntity(UserRequest userRequest, boolean hashPassword) {
        return UserEntity.builder()
                .email(userRequest.getEmail())
                .password(hashPassword ? credentialUtils.hash(userRequest.getPassword()) : userRequest.getPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .contacts(userRequest.getContacts())
                .balance(userRequest.getBalance())
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
