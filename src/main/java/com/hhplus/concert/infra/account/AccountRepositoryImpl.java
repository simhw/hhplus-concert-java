package com.hhplus.concert.infra.account;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.account.AccountRepository;
import com.hhplus.concert.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Account getAccount(User user) {
        return accountJpaRepository.findByUser(user).orElse(null);
    }

    @Override
    public Account getAccountForUpdate(User user) {
        return accountJpaRepository.findAccountForUpdate(user).orElse(null);
    }
}
