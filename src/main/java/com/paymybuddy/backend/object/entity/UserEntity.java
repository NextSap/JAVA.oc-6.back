package com.paymybuddy.backend.object.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserEntity {
    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @ElementCollection
    private List<String> contacts;
    private BigDecimal balance;
}
