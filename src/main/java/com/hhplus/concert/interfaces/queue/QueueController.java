package com.hhplus.concert.interfaces.queue;

import com.hhplus.concert.domain.queue.QueueInfo;
import com.hhplus.concert.domain.queue.QueueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Queue", description = "대기열 API")
@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    /**
     * 접속 대기 요청
     */
    @PostMapping("")
    public ResponseEntity<QueueDto.QueueResponse> waiting() {
        QueueDto.QueueResponse result = new QueueDto.QueueResponse("TOKEN");
        return ResponseEntity.ok(result);
    }

    /**
     * 접속 대기 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<QueueDto.QueueStatusResponse> status(@RequestHeader("Queue-Token") String token) {
        QueueInfo info = queueService.getQueueInfo(token);
        QueueDto.QueueStatusResponse result =
                new QueueDto.QueueStatusResponse(token, info.getStatus(), info.getWaitingPosition(), info.getExpectedWaitTimeSeconds());
        return ResponseEntity.ok(result);
    }
}
