package com.hhplus.concert.infra.outbox;

import com.hhplus.concert.domain.outbox.Outbox;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OutboxJpaRepository extends CrudRepository<Outbox, Long> {
    Optional<Outbox> findByPublisherId(Long publisherId);

    List<Outbox> findByIsPublished(boolean isPublished);
}
