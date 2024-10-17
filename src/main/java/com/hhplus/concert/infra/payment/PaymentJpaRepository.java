package com.hhplus.concert.infra.payment;

import com.hhplus.concert.domain.payment.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentJpaRepository extends CrudRepository<Payment, Long> {
}
