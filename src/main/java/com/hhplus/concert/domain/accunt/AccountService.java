package com.hhplus.concert.domain.accunt;

import com.hhplus.concert.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccount(User user) {
        Account account = accountRepository.getAccount(user);

        if (account == null) {
            throw new NoAccountException();
        }
        return account;
    }

    @Transactional
    public Account charge(User user, Integer amount) {
        Account account = getAccount(user);
        account.charge(amount);

        return account;
    }

    @Transactional
    public Account use(User user, Integer amount) {
        Account account = getAccount(user);
        account.use(amount);

        return account;
    }
}
