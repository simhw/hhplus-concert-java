package com.hhplus.concert.domain.queue;


public interface ActiveQueueRepository {
    Token getActiveQueueToken(Token token);

    boolean saveActiveQueueToken(Token token, long ttl);

    long getActiveQueueSize();
}
