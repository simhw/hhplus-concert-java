package com.hhplus.concert.infra.concert;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertPerformance;
import com.hhplus.concert.domain.concert.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertJpaRepository extends CrudRepository<Concert, Long> {
    List<Concert> findAllByDeletedAtIsNull();

    @Query("SELECT c FROM Concert c JOIN FETCH c.performances WHERE c.id = :concertId")
    Optional<Concert> findConcert(Long concertId);

    @Query("SELECT p FROM Concert c JOIN c.performances p WHERE c.id = :concertId AND p.id = :performanceId")
    Optional<ConcertPerformance> findPerformance(Long concertId, Long performanceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Optional<Seat> findSeatForUpdate(@Param("seatId") Long seatId);
}
