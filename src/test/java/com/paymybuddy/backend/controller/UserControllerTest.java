package com.paymybuddy.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.backend.object.TransferType;
import com.paymybuddy.backend.object.request.UserRequest;
import com.paymybuddy.backend.object.response.UserResponse;
import com.paymybuddy.backend.service.UserService;
import com.paymybuddy.backend.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String endpoint = "/user";
    private final JwtUtils jwtUtils = new JwtUtils("secret");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        userResponse = UserResponse.builder()
                .email("email")
                .firstName("Foo")
                .lastName("Bar")
                .contacts(new ArrayList<>())
                .balance(BigDecimal.valueOf(0.0))
                .build();

        userRequest = UserRequest.builder()
                .email("email")
                .password("password")
                .firstName("Foo")
                .lastName("Bar")
                .balance((double) 0)
                .contacts(new ArrayList<>())
                .build();
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.getUserResponseByToken()).thenReturn(userResponse);

        mockMvc.perform(get(endpoint).param("email", "email").header("Authorization", "Bearer " + jwtUtils.get("email", false)))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(userResponse)));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        when(userService.getMinimizedUserResponseByEmail("email")).thenReturn(userResponse);

        mockMvc.perform(get(endpoint + "/email").param("email", "email").header("Authorization", "Bearer " + jwtUtils.get("email", false)))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(userResponse)));
    }

    @Test
    public void addContact() throws Exception {
        when(userService.addContact("email")).thenReturn(userResponse);

        mockMvc.perform(post(endpoint + "/add-contact").param("email", "email").header("Authorization", "Bearer " + jwtUtils.get("email", false)))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(userResponse)));
    }

    @Test
    public void testUpdateUserEntity() throws Exception {
        when(userService.updateUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put(endpoint).contentType(MediaType.APPLICATION_JSON).content(OBJECT_MAPPER.writeValueAsString(userRequest))
                        .header("Authorization", "Bearer " + jwtUtils.get("email", false)))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(userResponse)));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser();
        mockMvc.perform(delete(endpoint).header("Authorization", "Bearer " + jwtUtils.get("email", false)))
                .andExpect(status().isOk());
    }

    @Test
    public void testTransferMoney() throws Exception {
        when(userService.transferMoney(any(TransferType.class), anyDouble())).thenReturn(userResponse);
        mockMvc.perform(post(endpoint + "/transfer-money").contentType(MediaType.APPLICATION_JSON).param("transferType", "WITHDRAWAL").param("amount", "10.0")
                        .header("Authorization", "Bearer " + jwtUtils.get("email", false)))
                .andExpect(status().isCreated())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(userResponse)));
    }
}