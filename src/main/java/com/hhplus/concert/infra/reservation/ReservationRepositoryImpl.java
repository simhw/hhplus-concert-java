package com.hhplus.concert.infra.reservation;

import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationRepository;
import com.hhplus.concert.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public Reservation getReservation(Long reservationId) {
        return reservationJpaRepository.findById(reservationId).orElse(null);
    }

    @Override
    public List<Reservation> getReservations(ReservationStatus status) {
        return reservationJpaRepository.findByStatus(status);
    }
}
