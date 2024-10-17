package com.hhplus.concert.domain.reservation;


import java.util.List;

public interface ReservationRepository {
    Reservation saveReservation(Reservation reservation);

    Reservation getReservation(Long reservationId);

    List<Reservation> getReservations(ReservationStatus status);
}
