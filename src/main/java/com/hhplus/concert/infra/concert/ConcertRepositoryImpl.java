package com.hhplus.concert.infra.concert;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.ConcertRepository;
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
        Optional<Concert> concert = concertJpaRepository.findByIdAndDeletedAtIsNull(concertId);
        return concert.orElse(null);
    }

    @Override
    public List<Concert> getConcerts() {
        return concertJpaRepository.findAllByDeletedAtIsNull();
    }
}
