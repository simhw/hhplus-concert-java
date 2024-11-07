package com.hhplus.concert.domain.queue;

import lombok.Getter;
import lombok.Setter;


@Getter
public class QueueInfo {
    private String status;

    @Setter
    private Long waitingPosition;
    @Setter
    private Long expectedWaitTimeSeconds;

    public static QueueInfo toQueueInfo(String status) {
        QueueInfo queueInfo = new QueueInfo();
        queueInfo.status = status;
        return queueInfo;
    }
}
