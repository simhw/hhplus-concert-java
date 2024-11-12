package com.hhplus.concert.domain.queue;


import java.util.List;

public interface WaitingQueueRepository {

    long getWaitingNumber(Token token);

    List<Token> getTopNWaitingQueueToken(int n);

    boolean saveWaitingQueueToken(Token token, long score);

    boolean removeWaitingQueueToken(Token token);

    long getWaitingQueueSize();
}
