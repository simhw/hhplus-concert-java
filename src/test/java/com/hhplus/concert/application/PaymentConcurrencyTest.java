package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationStatus;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.account.AccountJpaRepository;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.reservation.ReservationJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class PaymentConcurrencyTest {
    @Autowired
    PaymentFacade paymentFacade;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    ConcertJpaRepository concertJpaRepository;

    @Autowired
    ReservationJpaRepository reservationJpaRepository;

    User USER;
    Account ACCOUNT;
    Concert CONCERT;
    ConcertPerformance PERFORMANCE;
    Seat SEAT;
    Reservation RESERVATION;

    @BeforeEach
    void init() {
        LocalDateTime now = LocalDateTime.now();
        USER = userJpaRepository.save(new User("username", "email"));
        ACCOUNT = accountJpaRepository.save(new Account(150000L, USER));

        SEAT = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        PERFORMANCE = new ConcertPerformance(LocalDate.now(), now.plusMinutes(100), now.plusMinutes(250), List.of(SEAT));
        CONCERT = new Concert("concert", List.of(PERFORMANCE));
        concertJpaRepository.save(CONCERT);

        RESERVATION = new Reservation(ReservationStatus.PAYMENT_WAITING, SEAT.getPrice(), USER, SEAT);
        RESERVATION = reservationJpaRepository.save(RESERVATION);
    }

    @DisplayName("동시에 5번 충전 요청 시 1번만 성공한다.")
    @Test
    void 예약_결제() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        AtomicInteger success = new AtomicInteger(0);

        for (long i = 1; i <= 5; i++) {
            es.submit(() -> {
                try {
                    paymentFacade.pay(USER.getId(), RESERVATION.getId());
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