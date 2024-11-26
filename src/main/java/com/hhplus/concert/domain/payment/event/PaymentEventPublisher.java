package com.hhplus.concert.domain.payment.event;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentEventPublisher {
    void publish(PaymentEvent.Completed paymentEvent) throws JsonProcessingException;
}
