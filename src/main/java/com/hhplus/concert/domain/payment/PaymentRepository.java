package com.hhplus.concert.domain.payment;


public interface PaymentRepository {
    Payment getPayment(Long paymentId);

    Payment savePayment(Payment payment);
}
