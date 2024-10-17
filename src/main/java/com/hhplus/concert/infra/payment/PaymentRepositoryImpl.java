package com.hhplus.concert.infra.payment;

import com.hhplus.concert.domain.payment.Payment;
import com.hhplus.concert.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment getPayment(Long paymentId) {
        return paymentJpaRepository.findById(paymentId).orElse(null);
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
