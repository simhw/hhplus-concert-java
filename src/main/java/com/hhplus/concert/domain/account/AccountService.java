package com.hhplus.concert.domain.account;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountInfo getAccount(User user) {
        Account account = accountRepository.getAccount(user);
        if (account == null) {
            throw new CoreException(ErrorType.ACCOUNT_NOT_FOUND, user.getId());
        }
        return AccountInfo.toAccountInfo(account);
    }

    @Transactional
    public AccountInfo charge(User user, Integer amount) {
        Account account = accountRepository.getAccountForUpdate(user);
        if (account == null) {
            throw new CoreException(ErrorType.ACCOUNT_NOT_FOUND, user.getId());
        }
        account.charge(amount);
        return AccountInfo.toAccountInfo(account);
    }

    @Transactional
    public AccountInfo use(User user, Integer amount) {
        Account account = accountRepository.getAccountForUpdate(user);
        if (account == null) {
            throw new CoreException(ErrorType.ACCOUNT_NOT_FOUND, user.getId());
        }
        account.use(amount);
        return AccountInfo.toAccountInfo(account);
    }
}
