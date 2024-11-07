package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.reservation.ReservationInfo;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
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

import static org.assertj.core.api.Assertions.*;

@Rollback(value = false)
@SpringBootTest
class ReservationFacadeTest {

    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    ConcertJpaRepository concertJpaRepository;

    User user;
    Seat seat1, seat2, seat3, seat4;
    ConcertPerformance performance1, performance2;
    Concert concert;

    @BeforeEach
    void init() {
        user = new User("username", "email");
        userJpaRepository.save(user);

        seat1 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        seat2 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);
        seat3 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        seat4 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);

        performance1 = new ConcertPerformance(
                LocalDate.now(),
                LocalDateTime.now().plusMinutes(30),
                LocalDateTime.now().plusMinutes(250),
                List.of(seat1, seat2)
        );
        performance2 = new ConcertPerformance(
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
        command.setConcertId(concert.getId());
        command.setPerformanceId(performance1.getId());
        command.setSeatId(seat2.getId());

        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class, () -> reservationFacade.placeReservation(command));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PERFORMANCE_EXPIRED);
    }

    @DisplayName("예약 가능한 상태의 좌석은 예약 가능하다.")
    @Test
    void 예약_가능_좌석_예약() {
        // given
        ReservationCommand command = new ReservationCommand();
        command.setUserId(user.getId());
        command.setConcertId(concert.getId());
        command.setPerformanceId(performance2.getId());
        command.setSeatId(seat3.getId());

        // when
        ReservationInfo reserve = reservationFacade.placeReservation(command);

        // then
        assertThat(reserve.getId()).isNotNull();
    }

    @DisplayName("예약 상태의 좌석은 예약 'DUPLICATED_RESERVATION' 예외가 발생한다.")
    @Test
    void 예약_불가능_좌석_예약() {
        // given
        ReservationCommand command = new ReservationCommand();
        command.setUserId(user.getId());
        command.setConcertId(concert.getId());
        command.setPerformanceId(performance2.getId());
        command.setSeatId(seat4.getId());

        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class, () -> reservationFacade.placeReservation(command));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.DUPLICATED_RESERVATION);
    }
}