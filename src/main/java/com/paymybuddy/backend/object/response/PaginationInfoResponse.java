package com.paymybuddy.backend.object.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfoResponse {
    private int totalPages;
}
