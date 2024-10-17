package com.hhplus.concert.interfaces.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueueDto {
    @Data
    public static class QueueRequest {
        private String userId;
    }

    @Data
    @AllArgsConstructor
    public static class QueueResponse {
        private String token;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class QueueStatusResponse {
        private String code;
        private String status;
        private Long waitingNumber;
        private Long waitingExpectedWaiting;
    }
}
