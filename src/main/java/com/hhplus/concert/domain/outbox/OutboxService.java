package com.hhplus.concert.domain.outbox;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public Outbox writeOutbox(String publisher, Long publisherId, String title, String message) {
        Outbox outbox = new Outbox(publisher, publisherId, title, message);
        return outboxRepository.saveOutbox(outbox);
    }

    @Transactional
    public boolean publishedOutbox(Long publisherId) {
        outboxRepository.getQutbox(publisherId).published();
        return true;
    }
}
