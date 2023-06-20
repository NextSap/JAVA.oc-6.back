package com.paymybuddy.backend.object.model;

import com.paymybuddy.backend.object.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class TransactionModel {
    private String sender;
    private String receiver;
    private String description;
    private BigDecimal amount;
    private Long timestamp;
    private TransactionType transactionType;
}
