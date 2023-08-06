package com.paymybuddy.backend.service;

import com.paymybuddy.backend.mapper.TransactionMapper;
import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.repository.TransactionRepository;
import com.paymybuddy.backend.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceTest {
    private final TransactionMapper transactionMapper = TransactionMapper.getInstance();
    private final String token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2ODkwNzY5NjIsImV4cCI6MTY4OTk0MDk2MjAxOH0.mnlbgfUssAgTEDVydPON_9ywbDLl6pl38uYcif2lM4g";
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils = new JwtUtils("secret");
    private TransactionEntity transactionEntity;
    private TransactionRequest transactionRequest;

    @BeforeEach
    public void setUp() {
        transactionEntity = TransactionEntity.builder()
                .id(1L)
                .sender(UserEntity.builder().email("sender").build())
                .receiver(UserEntity.builder().email("receiver").build())
                .amount(BigDecimal.valueOf(1.0))
                .fees(BigDecimal.valueOf(1.0).multiply(BigDecimal.valueOf(0.005)))
                .description("description")
                .build();

        transactionRequest = TransactionRequest.builder()
                .sender("sender")
                .receiver("receiver")
                .amount(1.0)
                .description("description")
                .build();

        when(authentication.getCredentials()).thenReturn(token);
        when(jwtUtils.getToken()).thenReturn(token);
        when(jwtUtils.getEmail(anyString(), anyBoolean())).thenReturn("sender");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetTransactions() {
        when(transactionRepository.findAll().stream().toList()).thenReturn(List.of(transactionEntity));
        assertEquals(List.of(transactionMapper.toTransactionResponse(transactionEntity)), transactionService.getTransactions(1, 1));
    }

    @Test
    public void testGetTransactionsById() {
        when(transactionRepository.findById(any())).thenReturn(Optional.ofNullable(transactionEntity));
        assertEquals(transactionMapper.toTransactionResponse(transactionEntity), transactionService.getTransaction(1L));
    }
    @Test
    public void testCreateTransaction() {
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        when(userService.getUserEntityByEmail(anyString())).thenReturn(UserEntity.builder().email("sender").balance(BigDecimal.valueOf(2.0)).build());

        assertEquals(transactionMapper.toTransactionResponse(transactionEntity), transactionService.createTransaction(transactionRequest));
    }

    @Test
    public void testGetPaginationInfo() {
        when(transactionRepository.count()).thenReturn(0L);
        assertEquals(0, transactionService.getPaginationInfo(1).getTotalPages());
    }
}
