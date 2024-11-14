package com.hhplus.concert.infra.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hhplus.concert.domain.concert.ConcertInfo;
import com.hhplus.concert.domain.concert.ConcertPerformanceInfo;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisCacheConfig {

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Jackson2JsonRedisSerializer<List> concertSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, List.class) {
            @Override
            protected JavaType getJavaType(Class<?> clazz) {
                return TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, ConcertInfo.class);
            }
        };
        Jackson2JsonRedisSerializer<List> performanceSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, List.class) {
            @Override
            protected JavaType getJavaType(Class<?> clazz) {
                return TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, ConcertPerformanceInfo.class);
            }
        };

        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("concerts", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(60))
                        .disableCachingNullValues()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(concertSerializer)))
                .withCacheConfiguration("concert_performances", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(1))
                        .disableCachingNullValues()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(performanceSerializer)))
                .build();
    }

}
