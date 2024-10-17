package com.hhplus.concert.interfaces.account;

import com.hhplus.concert.application.AccountFacade;
import com.hhplus.concert.domain.accunt.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AccountController {

    private final AccountFacade accountFacadeService;

    /**
     * 잔액 조회
     */
    @GetMapping("/{userId}/account")
    public ResponseEntity<AccountDto.AccountResponse> account(@PathVariable Long userId) {
        Account account = accountFacadeService.account(userId);
        return ResponseEntity.ok(AccountDto.AccountResponse.builder()
                .amount(account.getAmount())
                .build());
    }

    /**
     * 잔액 충전
     */
    @PutMapping("/{userId}/account")
    public ResponseEntity<AccountDto.AccountResponse> charge(
            @PathVariable Long userId,
            @RequestBody AccountDto.AccountRequest request
    ) {
        Account charged = accountFacadeService.charge(userId, request.getAmount());
        AccountDto.AccountResponse result = AccountDto.AccountResponse.builder()
                .amount(charged.getAmount())
                .updatedAt(charged.getUpdatedAt())
                .build();
        return ResponseEntity.ok(result);
    }
}
