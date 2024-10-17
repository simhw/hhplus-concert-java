package com.hhplus.concert.domain.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueService queueService;

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void activate() {
        queueService.activate();
    }

}
