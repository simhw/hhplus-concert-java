package com.hhplus.concert.interfaces.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    /**
     * 접속 대기 요청
     */
    @PostMapping("")
    public ResponseEntity<QueueDto.QueueResponse> waiting(QueueDto.QueueRequest request) {
        QueueDto.QueueResponse result = new QueueDto.QueueResponse("QUEUE_CODE");
        return ResponseEntity.ok(result);
    }

    /**
     * 접속 대기 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<QueueDto.QueueStatusResponse> status(@RequestHeader("Queue-Code") String code) {
        QueueDto.QueueStatusResponse result = new QueueDto.QueueStatusResponse("QUEUE_CODE", "WAIT", 1L, 1L);
        return ResponseEntity.ok(result);
    }
}
