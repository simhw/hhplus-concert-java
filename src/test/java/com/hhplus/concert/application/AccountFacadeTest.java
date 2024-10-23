package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.account.AccountInfo;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.account.AccountJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountFacadeTest {

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    private User user;

    @BeforeEach
    void init() {
        user = new User("username", "email");
        userJpaRepository.save(user);
        accountJpaRepository.save(new Account(0L, user));
    }

    @Test
    void 계좌_조회() {
        AccountInfo account = accountFacade.getAccountInfo(user.getId());
        assertThat(account).isNotNull();
    }

    @Test
    void 계좌_충전() {
        // given
        int amount = 10000;
        // when
        AccountInfo account = accountFacade.charge(user.getId(), amount);

        // then
        assertThat(account).isNotNull();
        assertThat(account.getAmount()).isEqualTo(amount);
    }
}