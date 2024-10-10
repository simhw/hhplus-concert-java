package com.hhplus.concert.interfaces.concert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    /**
     * 콘서트 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ConcertDto.ConcertResponse>> concerts() {
        ConcertDto.ConcertResponse concert1 = new ConcertDto.ConcertResponse(1L, "concert1");
        ConcertDto.ConcertResponse concert2 = new ConcertDto.ConcertResponse(2L, "concert2");
        return ResponseEntity.ok(List.of(concert1, concert2));
    }

    /**
     * 콘서트 공연 목록 조회
     */
    @GetMapping("/{concertId}")
    public ResponseEntity<List<ConcertDto.PerformanceResponse>> performances(@RequestHeader("Queue-Code") String code, @PathVariable String concertId) {
        ConcertDto.PerformanceResponse performance1 = ConcertDto.PerformanceResponse.builder()
                .id(1L)
                .date(LocalDate.now())
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(2))
                .availableSeats(
                        List.of(
                                ConcertDto.SeatResponse.builder()
                                        .id(1L)
                                        .grade("BASIC")
                                        .no(1)
                                        .price(100000)
                                        .build(),
                                ConcertDto.SeatResponse.builder()
                                        .id(1L)
                                        .grade("VIP")
                                        .no(2)
                                        .price(150000)
                                        .build()
                        )
                )
                .build();

        ConcertDto.PerformanceResponse performance2 = ConcertDto.PerformanceResponse.builder()
                .id(2L)
                .date(LocalDate.now().plusDays(1))
                .startAt(LocalDateTime.now().plusDays(1))
                .endAt(LocalDateTime.now().plusDays(1).plusHours(2))
                .availableSeats(
                        List.of(
                                ConcertDto.SeatResponse.builder()
                                        .id(3L)
                                        .grade("BASIC")
                                        .no(1)
                                        .price(100000)
                                        .build(),
                                ConcertDto.SeatResponse.builder()
                                        .id(4L)
                                        .grade("VIP")
                                        .no(2)
                                        .price(150000)
                                        .build()
                        )
                )
                .build();

        return ResponseEntity.ok(List.of(performance1, performance2));
    }
}
