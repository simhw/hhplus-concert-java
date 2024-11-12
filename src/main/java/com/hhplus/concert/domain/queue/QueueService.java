package com.hhplus.concert.domain.queue;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final ActiveQueueRepository activeQueueRepository;

    private final int ACTIVATE_QUEUE_COUNT = 30;
    private final int ACTIVATE_QUEUE_MAX_COUNT = 150;
    private final int ACTIVATE_QUEUE_SECONDS = 60;
    private final int ACTIVATE_EXPIRED_SECONDS = 300;   // 5 * 60 = 300
    /**
     * 대기열 진입 요청
     * 대기열에 진입한 토큰이 없는 경우, 활성화열에 토큰을 저장한다.
     */
    public Token waitQueue(long timestamp) {
        Token waitingToken = new Token("WAITING");

        if (activeQueueRepository.getActiveQueueSize() < ACTIVATE_QUEUE_MAX_COUNT) {
            Token activeToken = waitingToken.updateStatus("ACTIVE");
            boolean saved = activeQueueRepository.saveActiveQueueToken(activeToken, ACTIVATE_EXPIRED_SECONDS);
            if (!saved) {
                throw new CoreException(ErrorType.FAIL_SAVE_QUEUE, activeToken.getValue());
            }
            return activeToken;
        }

        boolean saved = waitingQueueRepository.saveWaitingQueueToken(waitingToken, timestamp);
        if (!saved) {
            throw new CoreException(ErrorType.FAIL_SAVE_QUEUE, waitingToken.getValue());
        }
        return waitingToken;
    }

    /**
     * 토큰 상태 조회
     * 대기 번호가 0 이하인 경우 활성화열에서 해당 토큰을 조회합니다.
     */
    public QueueTokenInfo getQueueTokenInfo(String token) {
        Token waitingToken = new Token(token, "WAITING");
        long waitingNumber = waitingQueueRepository.getWaitingNumber(waitingToken);

        // 대기 상태
        if (waitingNumber >= 1) {
            QueueTokenInfo waitingQueueTokenInfo = QueueTokenInfo.of(waitingToken);
            waitingQueueTokenInfo.setWaitingNumber(waitingNumber);
            waitingQueueTokenInfo.setExpectedWaitingTimeSeconds(calExpectedWaitTimeSeconds(waitingNumber));
        }

        Token activeToken = new Token(token, "ACTIVE");
        Token findedActiveToken = activeQueueRepository.getActiveQueueToken(activeToken);
        if (findedActiveToken == null) {
            throw new CoreException(ErrorType.QUEUE_NOT_FOUND, activeToken.getValue());
        }
        // 활성화 상태
        return QueueTokenInfo.of(activeToken);
    }

    /**
     * 대기열 이동
     * 대기열에 토큰 저장을 실패한다면, 0순위로 다시 대기열에 저장합니다.
     */
    public void moveWaitingQueueToken() {
        List<Token> waitingQueueTokens = waitingQueueRepository.getTopNWaitingQueueToken(ACTIVATE_QUEUE_COUNT);

        waitingQueueTokens.forEach(waitingQueueToken -> {
            boolean saved = activeQueueRepository.saveActiveQueueToken(waitingQueueToken, ACTIVATE_EXPIRED_SECONDS);
            if (!saved) {
                waitingQueueRepository.saveWaitingQueueToken(waitingQueueToken, 0);
                return;
            }
            waitingQueueRepository.removeWaitingQueueToken(waitingQueueToken);
        });
    }

    /**
     * 예상 대기 시간 계산
     */
    private long calExpectedWaitTimeSeconds(long rank) {
        long turn = (long) Math.ceil(rank / (ACTIVATE_QUEUE_COUNT * 1.0));
        return turn * ACTIVATE_QUEUE_SECONDS;
    }

}
