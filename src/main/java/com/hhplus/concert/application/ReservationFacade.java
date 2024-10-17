package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertService;
import com.hhplus.concert.domain.concert.Performance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.queue.QueueService;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.reservation.ReservationService;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationFacade {

    private final QueueService queueService;
    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;

    /**
     * 콘서트 예약
     * 활성화되지 않은 토큰인 경우 실패를 반환한다.
     * TODO 동시성 문제 구현 예정
     */
    @Transactional
    public boolean reserve(ReservationCommand command) {
        queueService.verifyIsActive(command.getToken());
        User user = userService.getUser(command.getUserId());
        Seat seat = concertService.getAvailableSeat(command.getConcertId(), command.getPerformanceId(), command.getSeatId());
        seat.reserve();
        Reservation reserve = reservationService.reserve(user, seat);
        return reserve.getId() != null;
    }
}
