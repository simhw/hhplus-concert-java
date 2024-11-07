package com.hhplus.concert.interfaces.reservation;

import com.hhplus.concert.application.ReservationFacade;
import com.hhplus.concert.domain.reservation.ReservationCommand;
import com.hhplus.concert.domain.reservation.ReservationInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reservation", description = "예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    /**
     * 공연 좌석 예약
     */
    @PostMapping("")
    public ResponseEntity<ReservationDto.ReservationResponse> reserve(
            @RequestBody ReservationDto.ReservationRequest request
    ) {
        ReservationInfo info = reservationFacade.placeReservation(ReservationCommand.toReservationCommand(request));
        ReservationDto.ReservationResponse result = ReservationDto.ReservationResponse.builder()
                .id(info.getId())
                .status(info.getStatus())
                .build();
        return ResponseEntity.ok(result);
    }
}
