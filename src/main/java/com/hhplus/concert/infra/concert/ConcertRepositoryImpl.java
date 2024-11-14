package com.hhplus.concert.infra.concert;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.ConcertRepository;
import com.hhplus.concert.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Concert getConcert(Long concertId) {
        Optional<Concert> concert = concertJpaRepository.findConcert(concertId);
        return concert.orElse(null);
    }

    @Override
    public List<Concert> getConcerts() {
        return concertJpaRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public ConcertPerformance getPerformance(Long concertId, Long performanceId) {
        return concertJpaRepository.findPerformance(concertId, performanceId).orElse(null);
    }

    @Override
    public Seat getSeat(Long seatId) {
        return concertJpaRepository.findSeat(seatId).orElse(null);
    }

    @Override
    public Seat getSeatForUpdate(Long seatId) {
        return concertJpaRepository.findSeatForUpdate(seatId).orElse(null);
    }
}
