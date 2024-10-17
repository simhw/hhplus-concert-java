package com.hhplus.concert.interfaces.queue;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Queue", description = "대기열 API")
@RestController
@RequestMapping("/api/queue")
public class QueueController {
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
        QueueDto.QueueStatusResponse result =
                new QueueDto.QueueStatusResponse("TOKEN", "WAITING", 1L, 1L);
        return ResponseEntity.ok(result);
    }
}
