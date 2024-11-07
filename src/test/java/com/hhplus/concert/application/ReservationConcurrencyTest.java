package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.reservation.ReservationInfo;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.StopWatch;

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

    List<User> USERS;
    Concert CONCERT;
    ConcertPerformance PERFORMANCE;
    Seat SEAT;

    final int THREADS_COUNT = 250;

    @BeforeEach
    void init() {
        LocalDateTime now = LocalDateTime.now();

        USERS = new ArrayList<>();
        for (int i = 1; i <= THREADS_COUNT; i++) {
            User user = userJpaRepository.save(new User("USER" + i, null));
            USERS.add(user);
        }

        SEAT = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        PERFORMANCE = new ConcertPerformance(LocalDate.now(), now.plusMinutes(100), now.plusMinutes(250), List.of(SEAT));
        CONCERT = new Concert("CONCERT", List.of(PERFORMANCE));
        concertJpaRepository.save(CONCERT);
    }

    @Rollback(false)
    @DisplayName("동시에 서로 다른 유저 250명이 좌석 예약 시 1명만 성공한다.")
    @Test
    void 좌석_예약() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ExecutorService es = Executors.newFixedThreadPool(THREADS_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(THREADS_COUNT);
        AtomicInteger success = new AtomicInteger(0);

        // when
        for (int i = 0; i < THREADS_COUNT; i++) {
            int index = i;

            es.submit(() -> {
                try {
                    ReservationCommand command = new ReservationCommand();
                    command.setUserId(USERS.get(index).getId());
                    command.setConcertId(CONCERT.getId());
                    command.setPerformanceId(PERFORMANCE.getId());
                    command.setSeatId(SEAT.getId());
                    ReservationInfo info = reservationFacade.placeReservation(command);

                    if (info != null) {
                        success.incrementAndGet();
                    }

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

        Assertions.assertThat(success.get()).isEqualTo(1);
    }
}