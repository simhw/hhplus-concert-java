package com.hhplus.concert.infra.account;


import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountJpaRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByUser(User user);
}
