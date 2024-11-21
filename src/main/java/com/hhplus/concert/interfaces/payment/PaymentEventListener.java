package com.hhplus.concert.interfaces.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.application.event.PaymentEvent;
import com.hhplus.concert.domain.outbox.Outbox;
import com.hhplus.concert.domain.outbox.OutboxService;
import com.hhplus.concert.domain.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentEventListener {

    private final String TOPIC_NAME = "payments-topic";
    private final OutboxService outboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 아웃박스(보낸 메일함)를 생성한다.
     */
    @Async
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void writeOutbox(PaymentEvent.Completed event) {
        Outbox outbox = outboxService.writeOutbox(
                Payment.class.toString(),
                event.getPaymentId(),
                PaymentEvent.class.toString(),
                event.toString()
        );
    }

    /**
     * 이벤트 메시지를 발행한다.
     */
    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void publish(PaymentEvent.Completed event) {
        try {
            String eventAsJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC_NAME, eventAsJson);
        } catch (Exception e) {
            log.error("fail to serialize event", e);
        }
    }

}
