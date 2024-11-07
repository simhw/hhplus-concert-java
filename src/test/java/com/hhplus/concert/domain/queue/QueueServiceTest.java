package com.hhplus.concert.domain.queue;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    @DisplayName("새로운 토큰을 생성한 후 저장한다.")
    @Test
    void 대기열_등록() {
        // given
        String token = null;

        // when
        Queue queue = queueService.waitQueue(token);

        // then
        verify(queueRepository, times(1)).saveWaitingQueue(Mockito.any(Queue.class));
        Assertions.assertThat(queue.getToken()).isNotNull();
    }

    @DisplayName("기존에 발급된 토큰이 있는 경우 기존 토큰은 삭제시킨다.")
    @Test
    void 중복_대기열_등록() {
        // given
        String token = UUID.randomUUID().toString();
        Queue existed = new Queue(token);

        when(queueRepository.getWaitingNumber(token)).thenReturn(1L);

        // when
        queueService.waitQueue(token);

        // then
        verify(queueRepository, times(1)).saveWaitingQueue(Mockito.any(Queue.class));
        verify(queueRepository, times(1)).removeWaitingQueue(token);
    }

    @DisplayName("대기 상태에서 활성화 상태로 변경하며, 활성화된 일시를 추가한다.")
    @Test
    void 대기_상태_변경() {

    }
}