package com.hhplus.concert.domain.payment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentInfo {
    private Long id;
    private Integer amount;
    private LocalDateTime createdAt;

    public static PaymentInfo toPaymentInfo(Payment payment) {
        PaymentInfo info = new PaymentInfo();
        info.id = payment.getId();
        info.amount = payment.getAmount();
        info.createdAt = payment.getCreatedAt();
        return info;
    }
}
