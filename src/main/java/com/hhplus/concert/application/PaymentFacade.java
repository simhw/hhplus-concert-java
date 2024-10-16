package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.AccountService;
import com.hhplus.concert.domain.payment.Payment;
import com.hhplus.concert.domain.payment.PaymentService;
import com.hhplus.concert.domain.queue.QueueService;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationService;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PaymentFacade {

    private final QueueService queueService;
    private final UserService userService;
    private final AccountService accountService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    /**
     * 콘서트 예약 결제
     */
    @Transactional
    public Payment pay(String token, Long userId, Long reservationId) {
        queueService.verifyIsActive(token);
        User user = userService.getUser(userId);

        Reservation reservation = reservationService.getReservation(reservationId);
        accountService.use(user, reservation.getAmount());
        reservation.paymentCompleted();

        return paymentService.pay(reservation);
    }
}
