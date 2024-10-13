package com.hhplus.concert.domain.accunt;


import com.hhplus.concert.domain.user.User;

public interface AccountRepository {
    Account findByUser(User user);
}
