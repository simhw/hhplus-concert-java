package com.hhplus.concert.domain.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationService reservationService;

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void releaseSeatsOnPaymentExpired() {
        reservationService.releaseSeatsOnPaymentExpired();
    }
}
