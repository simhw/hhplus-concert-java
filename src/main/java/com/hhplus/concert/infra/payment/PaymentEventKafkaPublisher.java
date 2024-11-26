package com.hhplus.concert.infra.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert.domain.payment.event.PaymentEvent;
import com.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventKafkaPublisher implements PaymentEventPublisher {

    private static final String TOPIC_NAME = "payments-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(PaymentEvent.Completed event) throws JsonProcessingException {
        String data = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(TOPIC_NAME, data);
    }
}
