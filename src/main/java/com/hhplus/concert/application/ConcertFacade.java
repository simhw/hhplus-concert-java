package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    /**
     * 삭제되지 않은 콘서트 목록 조회
     */
    @Cacheable(value = "concerts", cacheNames = "concerts", key = "'all'")
    public List<ConcertInfo> getConcertInfos() {
        return concertService.getConcertInfos();
    }

    /**
     * 예약 가능한 공연 목록 조회
     */
    @Cacheable(value = "concert_performances", cacheNames = "concert_performances", key = "#concertId")
    public List<ConcertPerformanceInfo> getAvailablePerformanceInfos(Long concertId) {
        return concertService.getAvailablePerformanceInfos(concertId);
    }

    /**
     * 공연 좌석 목록 조회
     */
    public List<SeatInfo> getSeatInfos(Long concertId, Long performanceId) {
        return concertService.getSeatInfos(concertId, performanceId);
    }
}

