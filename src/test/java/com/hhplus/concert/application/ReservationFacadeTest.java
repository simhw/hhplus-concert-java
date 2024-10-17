package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.Performance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.concert.exception.ExpiredPerformanceException;
import com.hhplus.concert.domain.concert.exception.NotAvailableSeatException;
import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.queue.QueueJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Rollback(value = false)
@SpringBootTest
class ReservationFacadeTest {

    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    QueueJpaRepository queueJpaRepository;

    @Autowired
    ConcertJpaRepository concertJpaRepository;

    User user;
    Queue queue;
    Seat seat1, seat2, seat3, seat4;
    Performance performance1, performance2;
    Concert concert;

    @BeforeEach
    void init() {
        user = new User("username", "email");
        userJpaRepository.save(user);

        queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

        seat1 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        seat2 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);
        seat3 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        seat4 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);

        performance1 = new Performance(
                LocalDate.now(),
                LocalDateTime.now().plusMinutes(30),
                LocalDateTime.now().plusMinutes(250),
                List.of(seat1, seat2)
        );
        performance2 = new Performance(
                LocalDate.now(),
                LocalDateTime.now().plusMinutes(100),
                LocalDateTime.now().plusMinutes(250),
                List.of(seat3, seat4)
        );
        concert = new Concert("concert", List.of(performance1, performance2));
        concertJpaRepository.save(concert);
    }

    @DisplayName("공연 시작 시간 30분 이후 공연은 예약 불가능하다.")
    @Test
    void 예약_불가능_공연_예약() {
        // given
        ReservationCommand command = new ReservationCommand();
        command.setUserId(user.getId());
        command.setToken(queue.getToken());
        command.setConcertId(concert.getId());
        command.setPerformanceId(performance1.getId());
        command.setSeatId(seat2.getId());

        // when, then
        Assertions.assertThrows(ExpiredPerformanceException.class, () -> reservationFacade.reserve(command));
    }

    @DisplayName("예약 가능한 상태의 좌석은 예약 가능하다.")
    @Test
    void 예약_가능_좌석_예약() {
        // given
        ReservationCommand command = new ReservationCommand();
        command.setUserId(user.getId());
        command.setToken(queue.getToken());
        command.setConcertId(concert.getId());
        command.setPerformanceId(performance2.getId());
        command.setSeatId(seat3.getId());

        // when
        boolean reserved = reservationFacade.reserve(command);

        // then
        assertThat(reserved).isTrue();
    }

    @DisplayName("예약 상태의 좌석은 예약 불가능하다.")
    @Test
    void 예약_불가능_좌석_예약() {
        // given
        ReservationCommand command = new ReservationCommand();
        command.setUserId(user.getId());
        command.setToken(queue.getToken());
        command.setConcertId(concert.getId());
        command.setPerformanceId(performance2.getId());
        command.setSeatId(seat4.getId());

        // when, then
        Assertions.assertThrows(NotAvailableSeatException.class, () -> reservationFacade.reserve(command));
    }
}