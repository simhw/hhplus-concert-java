package com.hhplus.concert.application;

import com.hhplus.concert.domain.accunt.Account;
import com.hhplus.concert.domain.accunt.AccountService;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountFacadeService {

    private final UserService userService;
    private final AccountService accountService;

    public Account account(Long userId) {
        User user = userService.user(userId);
        return accountService.get(user);
    }

    public Account charge(Long userId, Integer amount) {
        User user = userService.user(userId);
        return accountService.charge(user, amount);
    }
}

