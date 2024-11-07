package com.hhplus.concert.infra.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.domain.queue.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueueRedisRepository {

    private static final String ACTIVE_QUEUE_KEY = "active_queue:";
    private static final String WAITING_QUEUE_KEY = "waiting_queue";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, String> redisTemplate;

    public Long getWaitingQueueRank(String token) {
        return redisTemplate.opsForZSet().rank(WAITING_QUEUE_KEY, token);
    }

    public List<Queue> getTopNWaitingQueues(int n) {
        Set<String> results = redisTemplate.opsForZSet().range(WAITING_QUEUE_KEY, 0, n - 1);
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }

        return results.stream()
                .map(Queue::new)
                .toList();
    }

    public Boolean addWaitingQueue(Queue queue) {
        long score = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return redisTemplate.opsForZSet().add(WAITING_QUEUE_KEY, queue.getToken(), score);
    }

    public Boolean removeWaitingQueue(String token) {
        Long result = redisTemplate.opsForZSet().remove(WAITING_QUEUE_KEY, token);
        return result != null && result > 0;
    }

    public Queue getActiveQueue(String token) {
        String key = ACTIVE_QUEUE_KEY + token;
        try {
            String result = redisTemplate.opsForValue().get(key);
            if (result == null || result.isEmpty()) {
                return null;
            }
            return objectMapper.readValue(result, Queue.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Boolean addActiveQueue(Queue queue, Duration ttl) {
        String key = ACTIVE_QUEUE_KEY + queue.getToken();
        try {
            String value = objectMapper.writeValueAsString(queue);
            return redisTemplate.opsForValue().setIfAbsent(key, value, ttl);
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
