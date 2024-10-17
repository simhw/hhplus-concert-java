package com.hhplus.concert.infra.reservation;


import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationJpaRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);
}
