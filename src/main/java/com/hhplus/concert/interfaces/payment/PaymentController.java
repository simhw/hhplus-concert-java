package com.hhplus.concert.interfaces.payment;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment", description = "결제 API")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    /**
     * 예약 결제
     */
    @PostMapping
    public ResponseEntity<String> pay(
            @RequestHeader("Queue-Token") String token,
            @RequestBody PaymentDto.PaymentRequest request
    ) {
        return ResponseEntity.ok("success");
    }
}
