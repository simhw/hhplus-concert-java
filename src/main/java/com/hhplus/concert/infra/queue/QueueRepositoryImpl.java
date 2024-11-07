package com.hhplus.concert.infra.queue;

import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.queue.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueRedisRepository queueRedisRepository;

    @Override
    public Queue getActiveQueue(String token) {
        return queueRedisRepository.getActiveQueue(token);
    }

    @Override
    public Long getWaitingNumber(String token) {
        return queueRedisRepository.getWaitingQueueRank(token);
    }

    @Override
    public List<Queue> getTopNWaitingQueue(int count) {
        return queueRedisRepository.getTopNWaitingQueues(count);
    }

    @Override
    public Boolean saveWaitingQueue(Queue queue) {
        return queueRedisRepository.addWaitingQueue(queue);
    }

    @Override
    public Boolean saveActiveQueue(Queue queue, int activeExpiredSeconds) {
        queueRedisRepository.addActiveQueue(queue, Duration.ofSeconds(activeExpiredSeconds));
        return null;
    }

    @Override
    public Boolean removeWaitingQueue(String token) {
        return queueRedisRepository.removeWaitingQueue(token);
    }
}
