package com.hhplus.concert.domain.account;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountInfo {
    private Long id;
    private Long amount;
    private LocalDateTime updatedAt;

    public static AccountInfo toAccountInfo(Account account) {
        AccountInfo info = new AccountInfo();
        info.setId(account.getId());
        info.setAmount(account.getAmount());
        info.setUpdatedAt(account.getUpdatedAt());
        return info;
    }
}
