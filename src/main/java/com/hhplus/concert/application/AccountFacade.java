package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.account.AccountService;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountFacade {

    private final UserService userService;
    private final AccountService accountService;

    public Account account(Long userId) {
        User user = userService.getUser(userId);
        return accountService.getAccount(user);
    }

    public Account charge(Long userId, Integer amount) {
        User user = userService.getUser(userId);
        return accountService.charge(user, amount);
    }
}

