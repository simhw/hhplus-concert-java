package com.hhplus.concert.domain.queue;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class QueueInfo {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    @Setter
    private Long waitingPosition;
    @Setter
    private Long expectedWaitTimeSeconds;

    public static QueueInfo toQueueInfo(Queue queue) {
        QueueInfo queueInfo = new QueueInfo();
        queueInfo.id = queue.getId();
        queueInfo.status = queue.getStatus().toString();
        queueInfo.createdAt = queue.getCreatedAt();
        return queueInfo;
    }
}
