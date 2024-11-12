package com.hhplus.concert.infra.queue;

import com.hhplus.concert.domain.queue.ActiveQueueRepository;
import com.hhplus.concert.domain.queue.Token;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@AllArgsConstructor
public class ActiveQueueRepositoryImpl implements ActiveQueueRepository {

    private final static String ACTIVE_QUEUE_KEY = "active_queue";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Token getActiveQueueToken(Token token) {
        String key = ACTIVE_QUEUE_KEY + ":" + token.getValue();
        String result = redisTemplate.opsForValue().get(key);
        return new Token(result, "ACTIVE");
    }

    @Override
    public boolean saveActiveQueueToken(Token token, long ttl) {
        String key = ACTIVE_QUEUE_KEY + ":" + token.getValue();
        redisTemplate.opsForValue().set(key, token.getValue(), ttl);
        return true;
    }

    @Override
    public long getActiveQueueSize() {
        Set<String> keys = redisTemplate.keys(ACTIVE_QUEUE_KEY + ":*");
        return (keys != null) ? keys.size() : 0;
    }
}
