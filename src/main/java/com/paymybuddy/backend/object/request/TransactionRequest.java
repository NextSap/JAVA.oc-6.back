package com.paymybuddy.backend.object.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class TransactionRequest {
    @NotNull(message = "sender:Null") @NotBlank(message = "sender:Required")
    private String sender;
    @NotNull(message = "receiver:Null") @NotBlank(message = "receiver:Required")
    private String receiver;
    @NotNull(message = "description:Null") @NotBlank(message = "description:Required")
    private String description;
    @NotNull(message = "amount:Null") @NotBlank(message = "amount:Required")
    private BigDecimal amount;
}
