package com.hhplus.concert.infra.outbox;

import com.hhplus.concert.domain.outbox.Outbox;
import com.hhplus.concert.domain.outbox.OutboxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox saveOutbox(Outbox outbox) {
        return outboxJpaRepository.save(outbox);
    }

    @Override
    public Outbox getQutbox(Long publisherId) {
        return outboxJpaRepository.findByPublisherId(publisherId).orElse(null);
    }

    @Override
    public List<Outbox> getOutboxes(boolean isPublished) {
        List<Outbox> outboxes = outboxJpaRepository.findByIsPublished(isPublished);
        return outboxes;
    }
}
