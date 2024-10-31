package com.hhplus.concert.application;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;

    /**
     * 좌석 예약
     */
    @Transactional
    public ReservationInfo placeReservation(ReservationCommand command) {
        User user = userService.getUser(command.getUserId());
        Seat seat = concertService.occupySeat(command.getConcertId(), command.getPerformanceId(), command.getSeatId());
        Reservation reserve = reservationService.placeReservation(user, seat);
        return ReservationInfo.toReservationInfo(reserve);
    }
}
