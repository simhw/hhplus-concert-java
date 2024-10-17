package com.hhplus.concert.interfaces.concert;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConcertDto {
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
    }

    @Data
    @Builder
    public static class SeatResponse {
        private Long id;
        private Integer no;
        private String grade;
        private Integer price;
        private String status;
    }
}
