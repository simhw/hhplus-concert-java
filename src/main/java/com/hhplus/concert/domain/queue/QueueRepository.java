package com.hhplus.concert.domain.queue;

import java.util.List;

public interface QueueRepository {

    Queue getActiveQueue(String token);

    Long getWaitingNumber(String token);

    List<Queue> getTopNWaitingQueue(int n);

    Boolean saveWaitingQueue(Queue queue);

    Boolean saveActiveQueue(Queue queue, int activeExpiredSeconds);

    Boolean removeWaitingQueue(String token);
}
