package com.paymybuddy.backend.object.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class TransactionResponse {
    private String sender;
    private String receiver;
    private String description;
    private BigDecimal amount;
    private BigDecimal fees;
    private Date date;
}
