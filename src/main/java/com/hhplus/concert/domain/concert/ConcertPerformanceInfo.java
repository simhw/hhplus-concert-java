package com.hhplus.concert.domain.concert;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConcertPerformanceInfo {
    private Long id;
    private LocalDate date;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static ConcertPerformanceInfo toConcertPerformanceInfo(ConcertPerformance performance) {
        ConcertPerformanceInfo info = new ConcertPerformanceInfo();
        info.setId(performance.getId());
        info.setDate(performance.getDate());
        info.setStartAt(performance.getStartAt());
        info.setEndAt(performance.getEndAt());
        return info;
    }
}
