package com.hhplus.concert.domain.payment;

import lombok.Getter;

public class PaymentEvent {

    @Getter
    public static class Completed {
        private Long paymentId;
        private Long reservationId;

        public Completed(Long paymentId, Long reservationId) {
            this.paymentId = paymentId;
            this.reservationId = reservationId;
        }
    }
}