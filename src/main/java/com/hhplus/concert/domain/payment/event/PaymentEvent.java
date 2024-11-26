package com.hhplus.concert.domain.payment.event;

import lombok.Getter;
import lombok.Setter;

public class PaymentEvent {
    @Getter
    @Setter
    public static class Completed {
        private Long paymentId;
        private Long reservationId;

        public Completed(Long paymentId, Long reservationId) {
            this.paymentId = paymentId;
            this.reservationId = reservationId;
        }
    }
}
