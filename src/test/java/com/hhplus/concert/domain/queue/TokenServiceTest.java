package com.hhplus.concert.domain.queue;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private WaitingQueueRepository waitingQueueRepository;

    @Mock
    private ActiveQueueRepository activeQueueRepository;

    @InjectMocks
    private QueueService queueService;

    @DisplayName("활성화열 사이즈가 150 미만인 경우 활성화열에 토큰을 저장한다.")
    @Test
    void 활성화열_진입() {
        // given
        long timestamp = System.currentTimeMillis();
        when(activeQueueRepository.getActiveQueueSize()).thenReturn(149L);
        when(activeQueueRepository.saveActiveQueueToken(any(Token.class), eq(300L))).thenReturn(true);

        // when
        Token token = queueService.waitQueue(timestamp);

        // then
        verify(activeQueueRepository, times(1)).saveActiveQueueToken(any(Token.class), eq(300L));
        assertThat(token.getStatus()).isEqualTo("ACTIVE");
    }

    @DisplayName("활성화열 사이즈가 150 이상인 경우 대기열에 토큰을 저장한다.")
    @Test
    void 대기열_진입() {
        // given
        long timestamp = System.currentTimeMillis();

        when(activeQueueRepository.getActiveQueueSize()).thenReturn(150L);
        when(waitingQueueRepository.saveWaitingQueueToken(any(Token.class), eq(timestamp))).thenReturn(true);

        // when
        Token token = queueService.waitQueue(timestamp);

        // then
        verify(waitingQueueRepository, times(1)).saveWaitingQueueToken(any(Token.class), eq(timestamp));
        assertThat(token.getStatus()).isEqualTo("WAITING");
    }

}