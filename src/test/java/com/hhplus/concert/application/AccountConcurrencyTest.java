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
import org.springframework.util.StopWatch;

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

    User USER;
    Account ACCOUNT;

    @BeforeEach
    void init() {
        USER = new User("USER", "USER@EXAMPLE.COM");
        userJpaRepository.save(USER);
        ACCOUNT = new Account(0L, USER);
        accountJpaRepository.save(ACCOUNT);
    }

    @DisplayName("동시에 1,000원을 5번 충전 요청 시 잔액은 5,000원이어야 한다.")
    @Test
    void 계좌_충전_비관적락() throws InterruptedException {
        final int THREADS_COUNT = 5;
        int AMOUNT = 1000;

        ExecutorService es = Executors.newFixedThreadPool(THREADS_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(THREADS_COUNT);
        AtomicInteger success = new AtomicInteger(0);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // when
        for (long i = 1; i <= THREADS_COUNT; i++) {
            es.submit(() -> {
                try {
                    accountFacade.charge(USER.getId(), AMOUNT);
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

        stopWatch.stop();
        System.out.println("소요시간: " + stopWatch.getTotalTimeMillis() + "ms");
        System.out.println(stopWatch.prettyPrint());

        // then
        Assertions.assertThat(success.get()).isEqualTo(THREADS_COUNT);
        Account account = accountJpaRepository.findByUser(USER).get();
        Assertions.assertThat(account.getAmount()).isEqualTo(THREADS_COUNT * AMOUNT);
    }
}
