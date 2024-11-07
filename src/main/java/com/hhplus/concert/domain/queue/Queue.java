package com.hhplus.concert.domain.queue;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Queue {

    private String token;

    protected Queue() {
    }

    public Queue(String token) {
        this.token = token;
    }

    public void generateQueueToken() {
        this.token = UUID.randomUUID().toString();
    }
}
