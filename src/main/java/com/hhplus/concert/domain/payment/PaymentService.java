package com.hhplus.concert.domain.payment;


import com.hhplus.concert.domain.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment pay(Reservation reservation) {
        Payment payment = new Payment(reservation.getAmount(), reservation);
        return paymentRepository.savePayment(payment);
    }
}
