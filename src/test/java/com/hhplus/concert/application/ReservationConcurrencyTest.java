package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class ReservationConcurrencyTest {

    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    ConcertJpaRepository concertJpaRepository;

    List<User> USERS = new ArrayList<>();
    Concert CONCERT;
    ConcertPerformance PERFORMANCE;
    Seat SEAT;

    @BeforeEach
    void init() {
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= 30; i++) {
            User user = userJpaRepository.save(new User("user" + i, "email" + i));
            USERS.add(user);
        }

        SEAT = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        PERFORMANCE = new ConcertPerformance(LocalDate.now(), now.plusMinutes(100), now.plusMinutes(250), List.of(SEAT));
        CONCERT = new Concert("concert", List.of(PERFORMANCE));
        concertJpaRepository.save(CONCERT);
    }

    @DisplayName("동시에 30명이 좌석 예약 시 1명만 성공한다.")
    @Test
    void 좌석_예약() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(30);
        CountDownLatch countDownLatch = new CountDownLatch(30);
        AtomicInteger success = new AtomicInteger(0);

        // when
        for (int i = 0; i < 30; i++) {
            int index = i;
            es.submit(() -> {
                try {
                    ReservationCommand command = new ReservationCommand();
                    command.setUserId(USERS.get(index).getId());
                    command.setConcertId(CONCERT.getId());
                    command.setPerformanceId(PERFORMANCE.getId());
                    command.setSeatId(SEAT.getId());
                    reservationFacade.reserve(command);
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