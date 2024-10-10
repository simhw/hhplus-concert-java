package com.hhplus.concert.interfaces.payment;

import lombok.Data;

@Data
public class PaymentDto {

    @Data
    public static class PaymentRequest {
        private Long userId;
        private Long reservationId;
    }

    @Data
    public static class PaymentResponse {
        private Long id;
    }
}
