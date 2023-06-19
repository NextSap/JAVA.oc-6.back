package com.paymybuddy.backend.object.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequest {
    private String sender;
    private String receiver;
    private String description;
    private BigDecimal amount;
}
