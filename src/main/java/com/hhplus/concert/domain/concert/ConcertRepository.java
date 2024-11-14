package com.hhplus.concert.domain.concert;

import java.util.List;

public interface ConcertRepository {
    Concert getConcert(Long concertId);

    List<Concert> getConcerts();

    ConcertPerformance getPerformance(Long concertId, Long performanceId);

    Seat getSeat(Long seatId);

    Seat getSeatForUpdate(Long seatId);
}
