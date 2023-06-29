package com.paymybuddy.backend.service;

import com.paymybuddy.backend.mapper.TransactionMapper;
import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.repository.TransactionRepository;
import com.paymybuddy.backend.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionServiceTest {

    private final String token = JwtUtils.getInstance().get("sender", true);
    private final TransactionMapper transactionMapper = TransactionMapper.getInstance();

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private JwtUtils jwtUtils = JwtUtils.getInstance();
    private TransactionEntity transactionEntity;
    private TransactionRequest transactionRequest;

    @BeforeEach
    public void setUp() {
        transactionEntity = TransactionEntity.builder()
                .id(1L)
                .sender("sender")
                .receiver("receiver")
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

        when(jwtUtils.getEmail(token, false)).thenReturn("sender");
        when(transactionRepository.findAll()).thenReturn(List.of(transactionEntity));
        when(transactionRepository.findAll(Pageable.ofSize(1))).thenReturn(new PageImpl<>(List.of(transactionEntity)));
    }

    @Test
    public void testGetTransactions() {
        assertEquals(List.of(transactionMapper.toTransactionResponse(transactionEntity)), transactionService.getTransactions(1, 1));
    }

    @Test
    public void testCreateTransaction() {
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        assertEquals(transactionMapper.toTransactionResponse(transactionEntity), transactionService.createTransaction(transactionRequest));
    }
}
