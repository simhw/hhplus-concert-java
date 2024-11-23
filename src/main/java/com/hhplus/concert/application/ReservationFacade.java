package com.hhplus.concert.application;

import com.hhplus.concert.domain.reservation.ReservedEvent;
import com.hhplus.concert.domain.concert.ConcertService;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.reservation.ReservationInfo;
import com.hhplus.concert.domain.reservation.ReservationService;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final ApplicationEventPublisher eventPublisher;
    private final TransactionTemplate transactionTemplate;

    /**
     * 좌석 예약
     */
    public ReservationInfo placeReservation(ReservationCommand command) {
        // 1. 회원을 조회한다.
        User user = userService.getUser(command.getUserId());
        // 2. '예약가능'한 상태의 좌석을 조회한다.
        Seat seat = concertService.getAvailableSeat(command.getConcertId(), command.getPerformanceId(), command.getSeatId());

        // 3. 예약 내역을 생성 및 예약 생성 이벤트를 발행한다.
        return transactionTemplate.execute(status -> {
            Reservation reservation = reservationService.placeReservation(user, seat);
            eventPublisher.publishEvent(new ReservedEvent(reservation.getId(), command.getSeatId()));
            return ReservationInfo.from(reservation);
        });
    }
}
