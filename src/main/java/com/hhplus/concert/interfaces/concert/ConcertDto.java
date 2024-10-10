package com.hhplus.concert.interfaces.concert;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConcertDto {

    @Data
    public static class ConcertRequest {
        private Long id;
    }

    @Data
    @Builder
    public static class ConcertResponse {
        private Long id;
        private String title;
    }

    @Data
    @Builder
    public static class PerformanceResponse {
        private Long id;
        private LocalDate date;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private List<SeatResponse> availableSeats = new ArrayList<>();
        private List<SeatResponse> unavailableSeats = new ArrayList<>();
    }

    @Data
    @Builder
    public static class SeatResponse {
        private Long id;
        private String grade;
        private Integer no;
        private Integer price;
    }
}
