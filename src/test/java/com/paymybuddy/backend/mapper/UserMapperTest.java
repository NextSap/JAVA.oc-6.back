package com.paymybuddy.backend.mapper;

import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {
    private final UserMapper userMapper = UserMapper.getInstance();

    private UserEntity userEntity;
    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .email("email")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(0.0))
                .build();

        userResponse = UserResponse.builder()
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(0.0))
                .build();

        userRequest = UserRequest.builder()
                .email("email")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(0.0))
                .build();
    }

    @Test
    public void testToUserResponse() {
        assertEquals(userResponse, userMapper.toUserResponse(userEntity));
    }

    @Test
    public void testToUserEntity() {
        assertEquals(userEntity, userMapper.toUserEntity(userRequest, false));
    }
}
