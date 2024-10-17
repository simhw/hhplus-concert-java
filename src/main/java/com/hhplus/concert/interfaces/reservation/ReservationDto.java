package com.hhplus.concert.interfaces.reservation;

import lombok.Builder;
import lombok.Data;

@Data
public class ReservationDto {
    @Data
    @Builder
    public static class ReservationRequest {
        private Long userId;
        private Long concertId;
        private Long performanceId;
        private Long seatId;
    }

    @Data
    @Builder
    public static class ReservationResponse {
        private Long id;
        private String status;
    }
}
