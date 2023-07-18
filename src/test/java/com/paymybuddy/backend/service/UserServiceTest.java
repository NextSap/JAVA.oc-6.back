package com.paymybuddy.backend.service;

import com.paymybuddy.backend.mapper.UserMapper;
import com.paymybuddy.backend.object.TransferType;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.LoginRequest;
import com.paymybuddy.backend.object.request.SigninRequest;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.repository.UserRepository;
import com.paymybuddy.backend.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    private final String token = new JwtUtils("secret").get("sender", false);
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private JwtUtils jwtUtils = new JwtUtils("secret");

    private UserEntity userEntity;

    @Mock
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .firstName("Foo")
                .lastName("Bar")
                .email("sender")
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(100))
                .password("$2a$10$bRa//G1hY8meV1vupT.4S.1Mmji6jNSO73RaL0WfLttNHZbWH/k1y") // test encoded
                .build();

        when(authentication.getCredentials()).thenReturn(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(jwtUtils.getToken()).thenReturn(token);
        when(jwtUtils.get(anyString(), anyBoolean())).thenReturn(token);
        when(jwtUtils.getEmail(anyString(), anyBoolean())).thenReturn("sender");
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
    }

    @Test
    public void testLogin() {
        when(bCryptPasswordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);

        assertNotNull(userService.login(LoginRequest.builder().email("sender").password("test").build()).getToken());
    }

    @Test
    public void testGetUser() {
        assertEquals(UserMapper.getInstance().toUserResponse(userEntity), userService.getUserResponseByToken());
        assertEquals(userEntity, userService.getUserEntityByEmail("sender"));
        assertEquals(UserMapper.getInstance().toMinimizedUserResponse(userEntity), userService.getMinimizedUserResponseByEmail("sender"));
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(UserEntity.builder().email("sender1").build());
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertEquals(UserResponse.builder().email("sender1").build(), userService.createUser(SigninRequest.builder().email("sender1").password("password").build()));
    }

    @Test
    public void testUpdateUserWithUserRequest() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        assertEquals(UserResponse.builder().email("sender").firstName("Foo").balance(BigDecimal.valueOf(0.0)).build(), userService.updateUser(UserRequest.builder().email("sender").password("password").balance(0d).firstName("Foo").build()));
    }

    @Test
    public void testUpdateUserWithUserEntity() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(UserEntity.builder().email("sender").firstName("Foo").build());

        assertEquals(UserResponse.builder().email("sender").firstName("Foo").build(), userService.updateUser(UserEntity.builder().email("sender").firstName("Foo").build()));
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).delete(any(UserEntity.class));
        userService.deleteUser();
        verify(userRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void testAddContact() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        assertEquals(UserMapper.getInstance().toUserResponse(userEntity), userService.addContact("sender1"));
    }

    @Test
    public void testTransferMoney() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        assertEquals(0, userEntity.getBalance().subtract(BigDecimal.valueOf(10)).compareTo(userService.transferMoney(TransferType.WITHDRAWAL, 10d).getBalance()));
    }
}
