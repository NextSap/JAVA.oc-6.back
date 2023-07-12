package com.paymybuddy.backend.mapper;

import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.entity.UserEntity;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionMapperTest {
    
    private final TransactionMapper transactionMapper = TransactionMapper.getInstance();
    
    private TransactionEntity transactionEntity;
    private TransactionResponse transactionResponse;
    private TransactionRequest transactionRequest;

    @BeforeEach
    public void setUp() {

        transactionEntity = TransactionEntity.builder()
                .amount(BigDecimal.valueOf(1.0))
                .sender(UserEntity.builder().email("sender").build())
                .receiver(UserEntity.builder().email("receiver").build())
                .description("description")
                .timestamp(0L)
                .fees(BigDecimal.valueOf(1.0).multiply(BigDecimal.valueOf(0.005)))
                .build();
        
        transactionResponse = TransactionResponse.builder()
                .amount(BigDecimal.valueOf(1.0))
                .sender("sender")
                .receiver("receiver")
                .description("description")
                .timestamp(0L)
                .fees(BigDecimal.valueOf(1.0).multiply(BigDecimal.valueOf(0.005)))
                .build();
        
        transactionRequest = TransactionRequest.builder()
                .amount(1.0)
                .sender("sender")
                .receiver("receiver")
                .description("description")
                .build();
    }

    @Test
    public void testToTransactionResponse() {
        assertEquals(transactionResponse, transactionMapper.toTransactionResponse(transactionEntity));
    }

    @Test
    public void testToTransactionEntity() {
        transactionEntity.setTimestamp(new Date().getTime());
        assertEquals(transactionEntity, transactionMapper.toTransactionEntity(transactionRequest, UserEntity.builder().email("sender").build(), UserEntity.builder().email("receiver").build()));
    }

    @Test
    public void testToTransactionResponseList() {
        assertEquals(List.of(transactionResponse), transactionMapper.toTransactionResponseList(List.of(transactionEntity)));
    }
}
