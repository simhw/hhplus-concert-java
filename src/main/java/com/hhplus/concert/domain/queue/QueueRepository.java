package com.hhplus.concert.domain.queue;

public interface QueueRepository {
    Queue getQueue(String token);

    Queue getFront();

    Queue addQueue(Queue queue);

    Iterable<Queue> getQueue(int count);
}
