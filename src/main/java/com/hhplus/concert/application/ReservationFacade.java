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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;

    /**
     * 콘서트 예약
     */
    @Transactional
    public ReservationInfo reserve(ReservationCommand command) {
        User user = userService.getUser(command.getUserId());
        Seat seat = concertService.getAvailableSeat(command.getConcertId(), command.getPerformanceId(), command.getSeatId());
        seat.reserve();
        Reservation reserve = reservationService.reserve(user, seat);
        return ReservationInfo.toReservationInfo(reserve);
    }
}
