package com.hhplus.concert.domain.queue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class QueueService {

    private final int ACTIVATE_QUEUE_COUNT = 30;
    private final int ACTIVATE_QUEUE_SECONDS = 60;  // 1분
    private final int ACTIVATE_EXPIRED_SECONDS = 300;   // 5분

    private final QueueRepository queueRepository;

    /**
     * 대기열 등록
     * 이미 등록된 대기가 있는 경우 기존 대기 정보는 삭제한다.
     */
    public Queue waitQueue(String token) {
        if (StringUtils.hasText(token)) {
            removeAlreadyWaitQueue(token);
        }

        Queue queue = new Queue();
        queue.generateQueueToken();
        queueRepository.saveWaitingQueue(queue);
        return queue;
    }

    private void removeAlreadyWaitQueue(String token) {
        long existed = queueRepository.getWaitingNumber(token);
        if (existed > 0) {
            queueRepository.removeWaitingQueue(token);
        }
    }

    /**
     * 대기 상태 조회
     * 대기 상태인 경우 대기 번호, 예상 대기 시간을 추가로 계산해 반환한다.
     * 대기열에 토큰이 존재하지 않는 경우, 활성화열 진입으로 가정한다.
     */
    public QueueInfo getQueueInfo(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("invalid token");
        }
        Long rank = queueRepository.getWaitingNumber(token);

        if (rank == null || rank <= 0) {
            QueueInfo activeQueueInfo = QueueInfo.toQueueInfo("ACTIVE");
            activeQueueInfo.setWaitingPosition(0L);
            activeQueueInfo.setExpectedWaitTimeSeconds(0L);
            return activeQueueInfo;
        }

        QueueInfo waitingQueueInfo = QueueInfo.toQueueInfo("WAITING");
        waitingQueueInfo.setWaitingPosition(rank);
        waitingQueueInfo.setExpectedWaitTimeSeconds(calExpectedWaitTimeSeconds(rank));
        return waitingQueueInfo;
    }

    private Long calExpectedWaitTimeSeconds(Long rank) {
        long turn = (long) Math.ceil(rank / (ACTIVATE_QUEUE_COUNT * 1.0));
        return turn * ACTIVATE_QUEUE_SECONDS;
    }

    /**
     * 작업 상태 전환
     * waiting queue → active queue
     */
    public void activateWaitingQueue() {
        List<Queue> topWaitingQueue = queueRepository.getTopNWaitingQueue(ACTIVATE_QUEUE_COUNT);
        log.info("activate waiting queue count:{}", topWaitingQueue.size());
        for (Queue queue : topWaitingQueue) {
            log.info("activate waiting queue token:{}", queue.getToken());
            queueRepository.saveActiveQueue(queue, ACTIVATE_QUEUE_SECONDS);
            queueRepository.removeWaitingQueue(queue.getToken());
        }
    }
}
