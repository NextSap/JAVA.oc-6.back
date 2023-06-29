package com.paymybuddy.backend.object.model;

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
}
