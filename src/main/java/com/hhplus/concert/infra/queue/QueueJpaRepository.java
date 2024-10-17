package com.hhplus.concert.infra.queue;

import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.queue.QueueStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends CrudRepository<Queue, Long> {
    Optional<Queue> findByToken(String token);

    List<Queue> findByStatus(QueueStatus status, Pageable pageable);

    Queue findFirstByStatusOrderByEnteredAtDesc(QueueStatus status);
}
