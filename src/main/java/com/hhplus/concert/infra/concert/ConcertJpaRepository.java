package com.hhplus.concert.infra.concert;

import com.hhplus.concert.domain.concert.Concert;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ConcertJpaRepository extends CrudRepository<Concert, Long> {
    List<Concert> findAllByDeletedAtIsNull();

    Optional<Concert> findByIdAndDeletedAtIsNull(Long id);

}
