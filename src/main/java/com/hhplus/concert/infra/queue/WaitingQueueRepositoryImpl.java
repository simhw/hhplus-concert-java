package com.hhplus.concert.infra.queue;

import com.hhplus.concert.domain.queue.Token;
import com.hhplus.concert.domain.queue.WaitingQueueRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final static String WAITING_QUEUE_KEY = "WAITING_QUEUE";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public long getWaitingNumber(Token token) {
        Long rank = redisTemplate.opsForZSet().rank(WAITING_QUEUE_KEY, token.getValue());
        return rank == null ? 0 : rank;
    }

    @Override
    public List<Token> getTopNWaitingQueueToken(int n) {
        Set<String> results = redisTemplate.opsForZSet().range(WAITING_QUEUE_KEY, 0, n - 1);
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.stream()
                .map((v) -> new Token(v, "WAITING"))
                .toList();
    }

    @Override
    public boolean saveWaitingQueueToken(Token token, long score) {
        redisTemplate.opsForZSet().add(WAITING_QUEUE_KEY, token.getValue(), score);
        return true;
    }

    @Override
    public boolean removeWaitingQueueToken(Token token) {
        redisTemplate.opsForZSet().remove(WAITING_QUEUE_KEY, token.getValue());
        return true;
    }

    @Override
    public long getWaitingQueueSize() {
        Long size = redisTemplate.opsForZSet().size(WAITING_QUEUE_KEY);
        return size == null ? 0 : size;
    }
}
