package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.payment.PaymentInfo;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationStatus;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.account.AccountJpaRepository;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.reservation.ReservationJpaRepository;
import com.hhplus.concert.infra.user.UserJpaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class PaymentFacadeTest {
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
    Seat SEAT1, SEAT2;

    @BeforeEach
    void init() {
        USER = new User("username", "email");
        userJpaRepository.save(USER);

        ACCOUNT = new Account(100000L, USER);
        accountJpaRepository.save(ACCOUNT);

        SEAT1 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        SEAT2 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);
        PERFORMANCE = new ConcertPerformance(LocalDate.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(250), List.of(SEAT1, SEAT2));
        CONCERT = new Concert("concert", List.of(PERFORMANCE));
        concertJpaRepository.save(CONCERT);
    }

    @DisplayName("예약건이 존재하는 경우 해당 예약에 대한 결제가 가능하다.")
    @Test
    void 예약_결제() {
        // given
        Reservation reservation = new Reservation(ReservationStatus.PAYMENT_WAITING, SEAT1.getPrice(), USER, SEAT1);
        reservationJpaRepository.save(reservation);

        // when
        PaymentInfo payment = paymentFacade.pay(USER.getId(), reservation.getId());
        assertThat(payment).isNotNull();
    }

    @DisplayName("잔액이 부족한 경우 'NOT_ENOUGH_ACCOUNT_AMOUNT' 예외가 발생한다.")
    @Test
    void 잔액_부족_예약_결제() {
        // given
        Reservation reservation = new Reservation(ReservationStatus.PAYMENT_WAITING, SEAT2.getPrice(), USER, SEAT2);
        reservationJpaRepository.save(reservation);

        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class,
                () -> paymentFacade.pay(USER.getId(), reservation.getId()));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_ENOUGH_ACCOUNT_AMOUNT);
    }
}