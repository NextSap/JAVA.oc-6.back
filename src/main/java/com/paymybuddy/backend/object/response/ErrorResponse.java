package com.paymybuddy.backend.object.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class ErrorResponse {
    private String field;
    private String cause;
}
