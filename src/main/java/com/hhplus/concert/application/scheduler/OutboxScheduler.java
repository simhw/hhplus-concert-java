package com.hhplus.concert.application.scheduler;

import com.hhplus.concert.domain.outbox.Outbox;
import com.hhplus.concert.domain.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final String TOPIC_NAME = "payments-topic";
    private final OutboxService outboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void publish() {
        List<Outbox> outboxes = outboxService.getPendingOutboxes();
        for (Outbox outbox : outboxes) {
            kafkaTemplate.send(TOPIC_NAME, outbox.getMessage());
        }
    }
}
