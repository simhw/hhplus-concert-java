package com.hhplus.concert.interfaces.payment;

import com.hhplus.concert.application.PaymentFacade;
import com.hhplus.concert.domain.payment.PaymentInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description = "결제 API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /**
     * 예약 결제
     */
    @PostMapping
    public ResponseEntity<PaymentDto.PaymentResponse> pay(
            @RequestBody PaymentDto.PaymentRequest request
    ) {
        PaymentInfo info = paymentFacade.pay(request.getUserId(), request.getReservationId());
        PaymentDto.PaymentResponse response = PaymentDto.PaymentResponse.builder().id(info.getId()).build();
        return ResponseEntity.ok(response);
    }
}
