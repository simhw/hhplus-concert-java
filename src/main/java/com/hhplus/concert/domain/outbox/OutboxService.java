package com.hhplus.concert.domain.outbox;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    @Transactional
    public Outbox writeOutbox(String publisher, Long publisherId, String title, String message) {
        Outbox outbox = new Outbox(publisher, publisherId, title, message);
        return outboxRepository.saveOutbox(outbox);
    }

    @Transactional
    public boolean publishedOutbox(Long publisherId) {
        outboxRepository.getQutbox(publisherId).published();
        return true;
    }

    public List<Outbox> getPendingOutboxes() {
        List<Outbox> outboxes = outboxRepository.getOutboxes(false);
        return outboxes.stream()
                .filter(v -> v.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5)))
                .toList();
    }
}
