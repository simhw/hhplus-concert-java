package com.hhplus.concert.interfaces.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    /**
     * 공연 좌석 예약
     */
    @PostMapping("")
    public ResponseEntity<ReservationDto.ReservationResponse> reserve(
            @RequestHeader("Queue-Code") String code,
            ReservationDto.ReservationResponse request
    ) {
        ReservationDto.ReservationResponse result = ReservationDto.ReservationResponse.builder()
                .id(1L)
                .status("PAYMENT_WAIT")
                .build();

        return ResponseEntity.ok(result);
    }

}
