package com.paymybuddy.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.PaginationInfoResponse;
import com.paymybuddy.backend.object.response.TransactionResponse;
import com.paymybuddy.backend.service.TransactionService;
import com.paymybuddy.backend.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private final String endpoint = "/transaction";

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String token = new JwtUtils("secret").get("email", false);

    @Test
    public void testGetTransaction() throws Exception {
        when(transactionService.getTransaction(any())).thenReturn(TransactionResponse.builder().build());

        mockMvc.perform(get(endpoint + "/1").header("Authorization", "Bearer " + token))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(TransactionResponse.builder().build())));
    }

    @Test
    public void testGetTransactions() throws Exception {
        when(transactionService.getTransactions(anyInt(), anyInt())).thenReturn(List.of(TransactionResponse.builder().build()));

        mockMvc.perform(get(endpoint).header("Authorization", "Bearer " + token).param("page", "1").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(TransactionResponse.builder().build()))));
    }

    @Test
    public void testGetPaginationInfo() throws Exception {
        when(transactionService.getPaginationInfo(anyInt())).thenReturn(PaginationInfoResponse.builder().totalPages(10).build());

        mockMvc.perform(get(endpoint + "/paginationInfo").header("Authorization", "Bearer " + token).param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(PaginationInfoResponse.builder().totalPages(10).build())));
    }

    @Test
    public void testPostTransaction() throws Exception {
        when(transactionService.createTransaction(any())).thenReturn(TransactionResponse.builder().build());

        mockMvc.perform(post(endpoint).header("Authorization", "Bearer " + token)
                        .contentType("application/json").content(objectMapper.writeValueAsString(TransactionRequest.builder().sender("sender").receiver("receiver").amount(0.1).description("d").build())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(TransactionResponse.builder().build())));
    }
}
