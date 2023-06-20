package com.paymybuddy.backend.mapper;

import com.paymybuddy.backend.object.TransactionType;
import com.paymybuddy.backend.object.entity.TransactionEntity;
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
                .sender("sender")
                .receiver("receiver")
                .description("description")
                .timestamp(new Date().getTime())
                .fees(BigDecimal.valueOf(1.0).multiply(BigDecimal.valueOf(0.005)))
                .transactionType(TransactionType.TRANSFER)
                .build();
        
        transactionResponse = TransactionResponse.builder()
                .amount(BigDecimal.valueOf(1.0))
                .sender("sender")
                .receiver("receiver")
                .description("description")
                .timestamp(new Date().getTime())
                .fees(BigDecimal.valueOf(1.0).multiply(BigDecimal.valueOf(0.005)))
                .transactionType(TransactionType.TRANSFER)
                .build();
        
        transactionRequest = TransactionRequest.builder()
                .amount(BigDecimal.valueOf(1.0))
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
        assertEquals(transactionEntity, transactionMapper.toTransactionEntity(transactionRequest, TransactionType.TRANSFER));
    }

    @Test
    public void testToTransactionResponseList() {
        assertEquals(List.of(transactionResponse), transactionMapper.toTransactionResponseList(List.of(transactionEntity)));
    }
}
