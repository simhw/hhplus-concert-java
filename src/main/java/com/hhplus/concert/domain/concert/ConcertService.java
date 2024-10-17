package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.concert.exception.NoConcertException;
import com.hhplus.concert.domain.concert.exception.NoPerformanceException;
import com.hhplus.concert.domain.concert.exception.NoSeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 콘서트 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Concert> getActiveConcerts() {
        return concertRepository.getConcerts();
    }

    /**
     * 콘서트 조회
     */
    @Transactional(readOnly = true)
    public Concert getActiveConcert(Long concertId) {
        Concert concert = concertRepository.getConcert(concertId);
        if (concert == null) {
            throw new NoConcertException();
        }
        return concert;
    }

    /**
     * 예약 가능한 공연 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Performance> getAvailablePerformances(Long concertId) {
        Concert concert = getActiveConcert(concertId);
        LocalDateTime now = LocalDateTime.now();
        return concert.getPerformances().stream()
                .filter(performance -> performance.isNotExpired(now))
                .toList();
    }

    /**
     * 예약 가능한 공연 조회
     */
    @Transactional(readOnly = true)
    public Performance getAvailablePerformance(Long concertId, Long performanceId) {
        Concert concert = getActiveConcert(concertId);
        LocalDateTime now = LocalDateTime.now();

        Performance performance = concert.getPerformances().stream()
                .filter(v -> v.getId().equals(performanceId))
                .findFirst()
                .orElseThrow(NoPerformanceException::new);

        performance.verifyIsNotExpired(now);
        return performance;
    }

    /**
     * 좌석 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Seat> getAllSeats(Long concertId, Long performanceId) {
        Performance performance = getAvailablePerformance(concertId, performanceId);
        return performance.getSeats();
    }

    /**
     * 예약 가능한 좌석 조회
     */
    @Transactional(readOnly = true)
    public Seat getAvailableSeat(Long concertId, Long performanceId, Long seatId) {
        Seat seat = getAllSeats(concertId, performanceId).stream()
                .filter(v -> v.getId().equals(seatId))
                .findFirst()
                .orElseThrow(NoSeatException::new);
        seat.verifyIsAvailable();
        return seat;
    }
}
