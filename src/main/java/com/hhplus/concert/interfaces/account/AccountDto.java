package com.hhplus.concert.interfaces.account;

import lombok.Builder;
import lombok.Data;

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
        private Long userId;
        private Integer amount;
    }
}
