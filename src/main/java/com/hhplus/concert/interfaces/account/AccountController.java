package com.hhplus.concert.interfaces.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/accounts")
public class AccountController {

    /**
     * 잔액 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<AccountDto.AccountResponse> account(@PathVariable Long userId) {
        return ResponseEntity.ok(AccountDto.AccountResponse.builder()
                .userId(userId)
                .amount(1000000)
                .build());
    }

    /**
     * 잔액 충전
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<AccountDto.AccountResponse> charge(@RequestBody AccountDto.AccountRequest request) {
        return ResponseEntity.ok(AccountDto.AccountResponse.builder()
                .userId(request.getUserId())
                .amount(1000000 + request.getAmount())
                .build());
    }
}
