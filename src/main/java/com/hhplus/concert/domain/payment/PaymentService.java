package com.hhplus.concert.domain.payment;


import com.hhplus.concert.domain.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    /**
     * 결제 정보 생성
     * */
    public PaymentInfo pay(Reservation reservation) {
        Payment payment = new Payment(reservation.getAmount(), reservation);
        Payment saved = paymentRepository.savePayment(payment);
        return PaymentInfo.toPaymentInfo(saved);
    }
}
