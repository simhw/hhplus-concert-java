package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.account.AccountJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class AccountConcurrencyTest {

    @Autowired
    private AccountFacade accountFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    private Long USER_ID;

    @BeforeEach
    void init() {
        User user = new User("admin", "admin");
        user = userJpaRepository.save(user);
        USER_ID = user.getId();

        Account account = new Account(10000L, user);
        accountJpaRepository.save(account);

    }
    @DisplayName("동시에 5번 충전 요청 시 1번만 성공한다.")
    @Test
    void 계좌_충전 () throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        AtomicInteger success = new AtomicInteger(0);

        // when
        for (long i = 1; i <= 5; i++) {
            es.submit(() -> {
                try {
                    accountFacade.charge(USER_ID, 10000);
                    success.incrementAndGet();

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        es.shutdown();

        Assertions.assertThat(success.get()).isEqualTo(1);
    }
}
