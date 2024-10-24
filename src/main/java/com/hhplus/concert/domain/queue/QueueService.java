package com.hhplus.concert.domain.queue;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QueueService {

    private final int ACTIVATE_QUEUE_COUNT = 30;
    private final int ACTIVATE_QUEUE_SECONDS = 60;  // 1분
    private final int ACTIVATE_EXPIRED_SECONDS = 300;   // 5분

    private final QueueRepository queueRepository;

    /**
     * 대기열 등록
     * 이미 등록된 대기가 있는 경우 만료 상태로 변경한다.
     */
    @Transactional
    public Queue wait(String token) {
        if (StringUtils.hasText(token)) {
            Queue existed = queueRepository.getQueue(token);
            existed.expire();
        }

        Queue queue = new Queue(UUID.randomUUID().toString());
        return queueRepository.addQueue(queue);
    }

    /**
     * 활성화 상태 체크
     */
    @Transactional(readOnly = true)
    public void verifyIsActive(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("queue token is null");
        }

        Queue queue = queueRepository.getQueue(token);
        if (queue == null) {
            throw new CoreException(ErrorType.QUEUE_NOT_FOUND, token);
        }
        queue.verifyIsActive(ACTIVATE_EXPIRED_SECONDS);
    }

    /**
     * 대기 상태 조회
     * 대기 상태인 경우 대기 번호, 예상 대기 시간을 추가로 계산해 반환한다.
     */
    @Transactional(readOnly = true)
    public QueueInfo getQueueInfo(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("token is null");
        }

        Queue queue = queueRepository.getQueue(token);
        if (queue == null) {
            throw new CoreException(ErrorType.QUEUE_NOT_FOUND, token);
        }

        QueueInfo info = QueueInfo.toQueueInfo(queue);
        if (queue.isWaiting()) {
            Queue front = queueRepository.getFront();
            info.setWaitingPosition(calWaitingNumber(queue, front));
            info.setExpectedWaitTimeSeconds(calExpectedWaitTimeSeconds(queue, front));
        }

        return info;
    }

    /**
     * 대기 번호 계산
     */
    private Long calWaitingNumber(Queue queue, Queue front) {
        if (queue.getId() > front.getId()) {
            long pos = queue.getId() - front.getId();
            return pos + 1;
        }
        return 1L;
    }

    /**
     * 예상 대기 시간 계산
     */
    private Long calExpectedWaitTimeSeconds(Queue queue, Queue front) {
        if (queue.getId() > front.getId()) {
            long pos = queue.getId() - front.getId();
            long turn = (long) Math.ceil(pos / (ACTIVATE_QUEUE_COUNT * 1.0));
            return turn * ACTIVATE_QUEUE_SECONDS;
        }
        return (long) ACTIVATE_QUEUE_SECONDS;
    }

    /**
     * 작업 상태 전환
     */
    @Transactional
    public void activate() {
        LocalDateTime now = LocalDateTime.now();
        Iterable<Queue> enqueue = queueRepository.getQueue(ACTIVATE_QUEUE_COUNT);
        enqueue.forEach(queue -> queue.activate(now));
    }

    /**
     * 일정 시간이 지난 경우 만료 처리
     */
    public void expire() {
    }
}
