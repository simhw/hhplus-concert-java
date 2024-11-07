package com.hhplus.concert.application;

import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.queue.QueueService;
import com.hhplus.concert.infra.queue.QueueRedisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class QueueServiceIntegrationTest {

    @Autowired
    private QueueService queueService;

    @Autowired
    private QueueRedisRepository queueRedisRepository;

    @Test
    @DisplayName("대기열 토큰을 발급받는다.")
    public void 대기열_진입() {
        String token = null;
        Queue queue = queueService.waitQueue(token);
        Assertions.assertThat(queue).isNotNull();

        List<Queue> waitingQueues = queueRedisRepository.getTopNWaitingQueues(1);
        Assertions.assertThat(waitingQueues.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("대기열에서 활성화열로 30개씩 이동한다.")
    public void 대기열_전환() {
        int waitingQueueCount = 30;
        // given
        for (int i = 0; i < waitingQueueCount; i++) {
            queueService.waitQueue(null);
        }

        // when
        queueService.activateWaitingQueue();
        List<Queue> waitingQueues = queueRedisRepository.getTopNWaitingQueues(waitingQueueCount);
        Assertions.assertThat(waitingQueues).hasSize(0);
    }
}
