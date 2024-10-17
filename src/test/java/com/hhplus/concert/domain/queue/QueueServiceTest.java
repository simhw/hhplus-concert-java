package com.hhplus.concert.domain.queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
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
        String token = "";

        // when
        Queue wait = queueService.wait(token);

        // then
        verify(queueRepository, times(1)).addQueue(Mockito.any(Queue.class));
    }

    @DisplayName("기존에 발급된 토큰이 있는 경우 기존 토큰은 만료시킨다.")
    @Test
    void 중복_대기열_등록() {
        // given
        String token = UUID.randomUUID().toString();
        Queue existed = new Queue(token);

        when(queueRepository.getQueue(token)).thenReturn(existed);

        // when
        Queue wait = queueService.wait(token);

        // then
        assertThat(existed.getStatus()).isEqualTo(QueueStatus.EXPIRED);
        verify(queueRepository, times(1)).addQueue(Mockito.any(Queue.class));
    }

    @DisplayName("대기 상태인 경우 'NotActivateQueueException' 에러가 발생한다.")
    @Test
    void 대기_상태_확인() {
        // given
        String token = UUID.randomUUID().toString();
        Queue queue = new Queue(token);
        when(queueRepository.getQueue(token)).thenReturn(queue);

        // when
        Assertions.assertThrows(NotActivateQueueException.class, () -> queueService.verifyIsActive(token));
    }

    @DisplayName("만료 상태인 경우 'NotActivateQueueException' 에러가 발생한다.")
    @Test
    void 만료_상태_확인() {
        // given
        String token = UUID.randomUUID().toString();
        Queue queue = new Queue(token);
        when(queueRepository.getQueue(token)).thenReturn(queue);

        // when
        Assertions.assertThrows(NotActivateQueueException.class, () -> queueService.verifyIsActive(token));
    }

    @DisplayName("대기 상태 조회 시 대기 번호, 대기 예상 시간 등을 반환한다.")
    @Test
    void 대기_상태_조회() {
        // given
        String token = UUID.randomUUID().toString();
        Queue queue = new Queue(100L, QueueStatus.WAITING, token, null);
        Queue front = new Queue(31L, QueueStatus.ACTIVE, null, LocalDateTime.now());

        when(queueRepository.getQueue(token)).thenReturn(queue);
        when(queueRepository.getFront()).thenReturn(front);

        // when
        QueueInfo info = queueService.getQueueInfo(token);

        // then
        assertThat(info).isNotNull();
        assertThat(info.getWaitingPosition()).isEqualTo(70);
        assertThat(info.getExpectedWaitTimeSeconds()).isEqualTo(180);
    }

    @DisplayName("대기 상태에서 활성화 상태로 변경하며, 활성화된 일시를 추가한다.")
    @Test
    void 대기_상태_변경() {
        // given
        List<Queue> queues = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String token = UUID.randomUUID().toString();
            Queue queue = new Queue(token);
            queues.add(queue);
        }

        when(queueRepository.getQueue(30)).thenReturn(queues);

        // when
        queueService.activate();

        // then
        for (Queue queue : queues) {
            assertThat(queue.getStatus()).isEqualTo(QueueStatus.ACTIVE);
            assertThat(queue.getEnteredAt()).isNotNull();
        }
    }
}