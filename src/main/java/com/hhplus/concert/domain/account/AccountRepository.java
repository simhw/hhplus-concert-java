package com.hhplus.concert.domain.account;


import com.hhplus.concert.domain.user.User;

public interface AccountRepository {
    Account getAccount(User user);
}
