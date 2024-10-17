package com.hhplus.concert.interfaces.account;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountDto {

    @Data
    @Builder
    public static class AccountRequest {
        private Long userId;
        private Integer amount;
    }

    @Data
    @Builder
    public static class AccountResponse {
        private Long amount;
        private LocalDateTime updatedAt;
    }
}
