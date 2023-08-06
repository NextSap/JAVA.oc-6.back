package com.paymybuddy.backend.object.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_email", referencedColumnName = "email")
    private UserEntity sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_email", referencedColumnName = "email")
    private UserEntity receiver;
    private String description;
    private BigDecimal amount;
    private BigDecimal fees;
    private Long timestamp;
}
