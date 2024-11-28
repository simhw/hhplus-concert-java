package com.hhplus.concert.domain.outbox;

import java.util.List;

public interface OutboxRepository {
    Outbox saveOutbox(Outbox outbox);

    Outbox getQutbox(Long publisherId);

    List<Outbox> getOutboxes(boolean isPublished);

}
