package com.hhplus.concert.interfaces.reservation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reservation", description = "예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    /**
     * 공연 좌석 예약
     */
    @PostMapping("")
    public ResponseEntity<ReservationDto.ReservationResponse> reserve(
            @RequestHeader("Queue-Token") String token,
            @RequestBody ReservationDto.ReservationRequest request
    ) {
        ReservationDto.ReservationResponse result = ReservationDto.ReservationResponse.builder()
                .id(1L)
                .status("PAYMENT_WAIT")
                .build();

        return ResponseEntity.ok(result);
    }
}
