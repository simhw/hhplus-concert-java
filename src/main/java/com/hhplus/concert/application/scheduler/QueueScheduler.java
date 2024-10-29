package com.hhplus.concert.application.scheduler;

import com.hhplus.concert.domain.queue.QueueService;
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
