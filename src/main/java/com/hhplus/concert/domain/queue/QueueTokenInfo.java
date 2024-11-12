package com.hhplus.concert.domain.queue;

import lombok.Data;

@Data
public class QueueTokenInfo {
    private String token;
    private String status;
    private long waitingNumber;
    private long expectedWaitingTimeSeconds;

    public static QueueTokenInfo of(Token token) {
        QueueTokenInfo tokenInfo = new QueueTokenInfo();
        tokenInfo.token = token.getValue();
        tokenInfo.status = token.getStatus();
        return tokenInfo;
    }
}
