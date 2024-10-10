package com.hhplus.concert.interfaces.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    /**
     * 예약 결제
     */
    @PostMapping
    public ResponseEntity<String> pay(
            @RequestHeader("Queue-Code") String code,
            PaymentDto.PaymentRequest request
    ) {
        return ResponseEntity.ok("success");
    }
}
