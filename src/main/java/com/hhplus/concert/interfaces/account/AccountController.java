package com.hhplus.concert.interfaces.account;

import com.hhplus.concert.application.AccountFacade;
import com.hhplus.concert.domain.account.AccountInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account", description = "계좌 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AccountController {

    private final AccountFacade accountFacadeService;

    /**
     * 잔액 조회
     */
    @GetMapping("/{userId}/account")
    public ResponseEntity<AccountDto.AccountResponse> account(
            @PathVariable Long userId
    ) {
        AccountInfo account = accountFacadeService.getAccountInfo(userId);
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
        AccountInfo charged = accountFacadeService.charge(userId, request.getAmount());
        AccountDto.AccountResponse result = AccountDto.AccountResponse.builder()
                .amount(charged.getAmount())
                .updatedAt(charged.getUpdatedAt())
                .build();
        return ResponseEntity.ok(result);
    }
}
