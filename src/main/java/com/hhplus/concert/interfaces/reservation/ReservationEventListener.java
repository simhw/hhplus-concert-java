package com.hhplus.concert.interfaces.reservation;

import com.hhplus.concert.application.event.ReservedEvent;
import com.hhplus.concert.domain.concert.ConcertService;
import com.hhplus.concert.domain.reservation.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.*;

@Slf4j
@Component
@AllArgsConstructor
public class ReservationEventListener {

    private final ConcertService concertService;
    private final ReservationService reservationService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void reserved(ReservedEvent event) {
        log.info("event published 1");
        try {
            concertService.occupySeat(event.getSeatId());
        } catch (Exception e) {
            reservationService.failReservation(event.getReservationId());
        }
    }
}
