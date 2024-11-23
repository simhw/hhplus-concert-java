package com.hhplus.concert.interfaces.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.domain.payment.PaymentEvent;
import com.hhplus.concert.domain.outbox.OutboxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentConsumer {

    private static final String TOPIC_NAME = "payments-topic";

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    /**
     * 자체 결제 토픽 수신
     * outbox 상태를 갱신한다.
     */
    @KafkaListener(topics = TOPIC_NAME, groupId = "concert-group1")
    public void self(String message) {
        log.info("message = {}", message);
        try {
            PaymentEvent.Completed event = objectMapper.readValue(message, PaymentEvent.Completed.class);
            outboxService.changeOutboxStatus(event.getPaymentId());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 결제 토픽 수신
     * 콘서트 좌석 상태를 변경한다.
     */
    @KafkaListener(topics = TOPIC_NAME, groupId = "concert-group2")
    public void consume(String message) {
        log.info("PaymentConsumer.consume");
        log.info("message = {}", message);
    }
}
