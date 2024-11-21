package com.hhplus.concert.application;

import com.hhplus.concert.domain.account.AccountService;
import com.hhplus.concert.domain.payment.PaymentInfo;
import com.hhplus.concert.domain.payment.PaymentService;
import com.hhplus.concert.domain.reservation.Reservation;
import com.hhplus.concert.domain.reservation.ReservationService;
import com.hhplus.concert.domain.user.User;
import com.hhplus.concert.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@AllArgsConstructor
public class PaymentFacade {

    private final TransactionTemplate transactionTemplate;
    private final UserService userService;
    private final AccountService accountService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    /**
     * 콘서트 예약 결제
     */
    public PaymentInfo pay(Long userId, Long reservationId) {
        // 1. 회원을 조회한다.
        User user = userService.getUser(userId);

        // start transaction()
        return transactionTemplate.execute(status -> {
            // 2. 예약 상태를 '결제완료' 상태로 변경한다.
            Reservation reservation = reservationService.completeReservation(reservationId);

            // 3. 잔고 금액을 차감한다.
            accountService.use(user, reservation.getAmount());

            // 4. 결제 내역을 생성한다.
            return paymentService.pay(reservation);
        });
    }
}
