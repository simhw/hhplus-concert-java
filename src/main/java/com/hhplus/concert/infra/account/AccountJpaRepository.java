package com.hhplus.concert.infra.account;


import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountJpaRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.user = :user")
    Optional<Account> findAccountForUpdate(User user);
}
