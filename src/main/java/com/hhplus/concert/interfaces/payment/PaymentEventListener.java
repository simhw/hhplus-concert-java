package com.hhplus.concert.interfaces.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.domain.payment.PaymentEvent;
import com.hhplus.concert.domain.outbox.OutboxService;
import com.hhplus.concert.domain.payment.Payment;
import com.hhplus.concert.domain.payment.PaymentEventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentEventListener {

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;
    private final PaymentEventPublisher paymentEventPublisher;

    /**
     * 아웃박스(보낸 메일함)를 생성한다.
     */
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void writeOutbox(PaymentEvent.Completed event) throws JsonProcessingException {
        outboxService.writeOutbox(
                Payment.class.toString(),
                event.getPaymentId(),
                PaymentEvent.class.toString(),
                objectMapper.writeValueAsString(event)
        );
    }

    /**
     * 이벤트 메시지를 발행한다.
     */
    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void publish(PaymentEvent.Completed event) throws JsonProcessingException {
        paymentEventPublisher.publish(event);
    }
}
