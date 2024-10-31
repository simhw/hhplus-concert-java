package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.ConcertService;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.reservation.ReservationInfo;
import com.hhplus.concert.domain.reservation.ReservationService;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;

    private static final String LOCK_KEY = "LOCK:";
    private final RedissonClient redissonClient;
    private final TransactionTemplate transactionTemplate;

    /**
     * 좌석 예약
     */
    @Transactional
    public ReservationInfo placeReservation(ReservationCommand command) {
        RLock lock = redissonClient.getLock(LOCK_KEY + command.getSeatId());

        try {

            // 락 획득을 시도하면 성공한 경우 예약을 실행한다.
            if (lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                return transactionTemplate.execute(status -> {
                    // biz logic
                    User user = userService.getUser(command.getUserId());
                    Seat seat = concertService.occupySeat(command.getConcertId(), command.getPerformanceId(), command.getSeatId());
                    Reservation reserve = reservationService.placeReservation(user, seat);
                    return ReservationInfo.toReservationInfo(reserve);
                });
            } else {
                // 락 획득을 실패한 경우 예약을 실패 처리한다.
                throw new CoreException(ErrorType.CONFLICT_RESERVATION, command.getSeatId());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return null;
    }
}
