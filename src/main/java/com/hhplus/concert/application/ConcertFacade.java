package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.*;
import com.hhplus.concert.domain.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertFacade {

    private final QueueService queueService;
    private final ConcertService concertService;

    /**
     * 삭제되지 않은 콘서트 목록 조회
     */
    public List<ConcertInfo> getConcertInfos(String token) {
        queueService.verifyIsActive(token);
        return concertService.getConcertInfos();
    }

    /**
     * 예약 가능한 공연 목록 조회
     */
    public List<ConcertPerformanceInfo> getAvailablePerformanceInfos(String token, Long concertId) {
        queueService.verifyIsActive(token);
        return concertService.getAvailablePerformanceInfos(concertId);
    }

    /**
     * 공연 좌석 목록 조회
     */
    public List<SeatInfo> getSeatInfos(String token, Long concertId, Long performanceId) {
        queueService.verifyIsActive(token);
        return concertService.getSeatInfos(concertId, performanceId);
    }
}

