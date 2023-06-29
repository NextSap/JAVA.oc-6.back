package com.paymybuddy.backend.mapper;

import com.paymybuddy.backend.object.entity.TransactionEntity;
import com.paymybuddy.backend.object.request.TransactionRequest;
import com.paymybuddy.backend.object.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionMapper {

    private static final TransactionMapper INSTANCE = new TransactionMapper();

    private TransactionMapper() {
    }

    public TransactionResponse toTransactionResponse(TransactionEntity transactionEntity) {
        return TransactionResponse.builder()
                .id(transactionEntity.getId())
                .sender(transactionEntity.getSender())
                .receiver(transactionEntity.getReceiver())
                .amount(transactionEntity.getAmount())
                .fees(transactionEntity.getFees())
                .description(transactionEntity.getDescription())
                .timestamp(transactionEntity.getTimestamp())
                .build();
    }

    public List<TransactionResponse> toTransactionResponseList(List<TransactionEntity> transactionEntityList) {
        return transactionEntityList.stream().map(this::toTransactionResponse).toList();
    }

    public TransactionEntity toTransactionEntity(TransactionRequest transactionRequest) {
        double fees = 0.005;
        return TransactionEntity.builder()
                .sender(transactionRequest.getSender())
                .receiver(transactionRequest.getReceiver())
                .amount(BigDecimal.valueOf(transactionRequest.getAmount()))
                .fees(BigDecimal.valueOf(transactionRequest.getAmount()).multiply(BigDecimal.valueOf(fees)))
                .description(transactionRequest.getDescription())
                .timestamp(new Date().getTime())
                .build();
    }

    public static TransactionMapper getInstance() {
        return INSTANCE;
    }
}
