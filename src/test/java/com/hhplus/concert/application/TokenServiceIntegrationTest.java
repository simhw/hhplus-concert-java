package com.hhplus.concert.application;

import com.hhplus.concert.domain.queue.ActiveQueueRepository;
import com.hhplus.concert.domain.queue.QueueService;
import com.hhplus.concert.domain.queue.Token;
import com.hhplus.concert.domain.queue.WaitingQueueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TokenServiceIntegrationTest {

    @Autowired
    private QueueService queueService;

    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    @Autowired
    private ActiveQueueRepository activeQueueRepository;

    @Test
    @DisplayName("대기열 토큰을 발급받는다.")
    public void 대기열_진입() {
        long timestamp = System.currentTimeMillis();
        Token queue = queueService.waitQueue(timestamp);
        Assertions.assertThat(queue).isNotNull();

        List<Token> waitingTokens = waitingQueueRepository.getTopNWaitingQueueToken(1);
        Assertions.assertThat(waitingTokens.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("대기열에서 활성화열로 30개씩 이동한다.")
    public void 대기열_전환() {
        // given
        long timestamp = System.currentTimeMillis();
        int waitingQueueCount = 30;
        for (int i = 0; i < waitingQueueCount; i++) {
            queueService.waitQueue(timestamp);
        }

        // when
        queueService.moveWaitingQueueToken();

        // then
        List<Token> waitingTokens = waitingQueueRepository.getTopNWaitingQueueToken(waitingQueueCount);
        Assertions.assertThat(waitingTokens).hasSize(0);
    }
}
