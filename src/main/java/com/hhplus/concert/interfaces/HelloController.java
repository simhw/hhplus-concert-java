package com.hhplus.concert.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public ResponseEntity<String> hello() {
        log.info("hello world!");
        return ResponseEntity.ok("hello world!");
    }
}
