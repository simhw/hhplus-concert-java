package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
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
    public List<ConcertInfo> getConcertInfos() {
        List<Concert> concerts = concertRepository.getConcerts();
        return concerts.stream()
                .map(ConcertInfo::toConcertInfo)
                .toList();
    }

    /**
     * 콘서트 조회
     */
    @Transactional(readOnly = true)
    public Concert getConcert(Long concertId) {
        Concert concert = concertRepository.getConcert(concertId);
        if (concert == null) {
            throw new CoreException(ErrorType.CONCERT_NOT_FOUND, concertId);
        }
        concert.verifyIsAvailable();
        return concert;
    }

    /**
     * 예약 가능한 공연 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ConcertPerformanceInfo> getAvailablePerformanceInfos(Long concertId) {
        Concert concert = getConcert(concertId);
        LocalDateTime now = LocalDateTime.now();
        return concert.getPerformances().stream()
                .filter(v -> v.isNotExpired(now))
                .map(ConcertPerformanceInfo::toConcertPerformanceInfo)
                .toList();
    }

    /**
     * 예약 가능한 공연 조회
     * 유효한 콘서트, 예약 가능한 공연을 조회합니다.
     */
    @Transactional(readOnly = true)
    public ConcertPerformance getAvailablePerformance(Long concertId, Long performanceId) {
        ConcertPerformance performance = concertRepository.getPerformance(concertId, performanceId);
        if (performance == null) {
            throw new CoreException(ErrorType.CONCERT_PERFORMANCE_NOT_FOUND, performanceId);
        }
        performance.getConcert().verifyIsAvailable();
        performance.verifyIsNotExpired(LocalDateTime.now());
        return performance;
    }

    /**
     * 좌석 목록 조회
     */
    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatInfos(Long concertId, Long performanceId) {
        ConcertPerformance performance = getAvailablePerformance(concertId, performanceId);
        return performance.getSeats().stream()
                .map(SeatInfo::toSeatInfo)
                .toList();
    }

    /**
     * 예약 가능한 좌석 조회
     * 유효한 콘서트, 예약 가능한 공연, 좌석을 조회한 후 좌석의 상태를 'RESERVED'로 변경합니다.
     */
    @Transactional
    public Seat occupySeat(Long concertId, Long performanceId, Long seatId) {
        ConcertPerformance performance = getAvailablePerformance(concertId, performanceId);
        Seat seat = performance.getSeats().stream()
                .filter(v -> v.getId().equals(seatId))
                .findFirst()
                .orElseThrow(() -> new CoreException(ErrorType.SEAT_NOT_FOUND, seatId));
        seat.occupy();
        return seat;
    }
}
