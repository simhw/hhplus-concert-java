package com.hhplus.concert.domain.queue;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Getter
public class Token {

    private String value;
    private String status;

    public Token(String status) {
        generateQueueToken();
        this.status = status;
    }

    public Token(String value, String status) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("token value can't be null");
        }
        this.value = value;
        this.status = status;
    }

    private void generateQueueToken() {
        this.value = UUID.randomUUID().toString();
    }

    public Token updateStatus(String status) {
        return new Token(this.value, status);
    }
}
