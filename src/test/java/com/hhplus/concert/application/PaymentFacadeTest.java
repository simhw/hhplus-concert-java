package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.Account;
import com.hhplus.concert.domain.account.NotEnoughAccountAmount;
import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.Performance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.payment.Payment;
import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationStatus;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.infra.account.AccountJpaRepository;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.queue.QueueJpaRepository;
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
import java.util.UUID;

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
    QueueJpaRepository queueJpaRepository;

    @Autowired
    ConcertJpaRepository concertJpaRepository;

    @Autowired
    ReservationJpaRepository reservationJpaRepository;

    User user;
    Queue queue;
    Seat seat1;
    Seat seat2;
    Seat seat3;
    Performance performance;
    Concert concert;

    @BeforeEach
    void init() {
        user = new User("username", "email");
        userJpaRepository.save(user);
        accountJpaRepository.save(new Account(150000L, user));

        queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

        seat1 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        seat2 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);
        seat3 = new Seat("VIP", 3, 250000, SeatStatus.RESERVED);

        performance = new Performance(LocalDate.now(), LocalDateTime.now().plusMinutes(100), LocalDateTime.now().plusMinutes(250), List.of(seat1, seat2));
        concert = new Concert("concert", List.of(performance));
        concertJpaRepository.save(concert);
    }

    @DisplayName("예약건이 존재하는 경우 결제가 가능하다.")
    @Test
    void 예약_결제() {
        // given
        Reservation reservation = new Reservation(ReservationStatus.PAYMENT_WAITING, seat2.getPrice(), user, seat2);
        reservationJpaRepository.save(reservation);

        // when
        Payment payment = paymentFacade.pay(queue.getToken(), user.getId(), reservation.getId());
        assertThat(payment).isNotNull();
        assertThat(payment.getReservation().getStatus()).isEqualTo(ReservationStatus.PAYMENT_COMPLETED);
    }

    @DisplayName("잔액이 부족한 경우 결제가 불가능하다.")
    @Test
    void 잔액_부족_예약_결제() {
        // given
        Reservation reservation = new Reservation(ReservationStatus.PAYMENT_WAITING, seat3.getPrice(), user, seat2);
        reservationJpaRepository.save(reservation);

        // when, then
        Assertions.assertThrows(NotEnoughAccountAmount.class, () -> paymentFacade.pay(queue.getToken(), user.getId(), reservation.getId()));
    }
}