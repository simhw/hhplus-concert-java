package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertService;
import com.hhplus.concert.domain.concert.Performance;
import com.hhplus.concert.domain.concert.Seat;
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
    public List<Concert> getActiveConcerts(String token) {
        queueService.verifyIsActive(token);
        return concertService.getActiveConcerts();
    }

    /**
     * 예약 가능한 공연 목록 조회
     */
    public List<Performance> getAvailablePerformances(String token, Long concertId) {
        queueService.verifyIsActive(token);
        return concertService.getAvailablePerformances(concertId);
    }

    /**
     * 공연 좌석 목록 조회
     */
    public List<Seat> getAllSeats(String token, Long concertId, Long performanceId) {
        queueService.verifyIsActive(token);
        return concertService.getAllSeats(concertId, performanceId);
    }
}

