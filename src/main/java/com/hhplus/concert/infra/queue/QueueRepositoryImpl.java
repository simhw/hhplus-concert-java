package com.hhplus.concert.infra.queue;

import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.queue.QueueRepository;
import com.hhplus.concert.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Queue getQueue(String token) {
        return queueJpaRepository.findByToken(token).orElse(null);
    }

    @Override
    public Queue getFront() {
        return queueJpaRepository.findFirstByStatusOrderByEnteredAtDesc(QueueStatus.WAITING);
    }

    @Override
    public Queue addQueue(Queue queue) {
        return queueJpaRepository.save(queue);
    }

    @Override
    public Iterable<Queue> getQueue(int count) {
        Pageable pageable = PageRequest.of(0, count, Sort.by("createdAt").ascending());
        return queueJpaRepository.findByStatus(QueueStatus.WAITING, pageable);
    }
}
