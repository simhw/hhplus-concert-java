package com.hhplus.concert.domain.reservation;

import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final int PAYMENT_TIMEOUT_SECONDS = 300;   // 5분
    private final ReservationRepository reservationRepository;

    public Reservation getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.getReservation(reservationId);
        if (reservation == null) {
            throw new CoreException(ErrorType.RESERVATION_NOT_FOUND, reservation);
        }
        return reservation;
    }

    /**
     * 좌석 예약
     */
    @Transactional
    public Reservation placeReservation(User user, Seat seat) {
        Reservation reservation = new Reservation(ReservationStatus.PAYMENT_WAITING, seat.getPrice(), user, seat);
        return reservationRepository.saveReservation(reservation);
    }

    /**
     * 일정 시간까지 결제되지 않은 좌석은 예약 가능한 상태로 변경한다.
     */
    @Transactional
    public void releaseSeatsOnPaymentExpired() {
        LocalDateTime expireTime = LocalDateTime.now().minusSeconds(PAYMENT_TIMEOUT_SECONDS);
        List<Reservation> reservations = reservationRepository.getReservations(ReservationStatus.PAYMENT_WAITING);

        List<Reservation> expires = reservations.stream()
                .filter(v -> v.isExpiredToPay(expireTime))
                .toList();

        expires.forEach(reservation -> {
            Seat seat = reservation.getSeat();
            seat.release();
            reservation.expire();
        });
    }
}
