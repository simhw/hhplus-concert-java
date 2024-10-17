package com.hhplus.concert.interfaces.concert;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Concert", description = "콘서트 API")
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    /**
     * 콘서트 목록 조회
     */
    @GetMapping("")
    public ResponseEntity<List<ConcertDto.ConcertResponse>> concerts(@RequestHeader("Queue-Token") String token) {
        ConcertDto.ConcertResponse concert1 = new ConcertDto.ConcertResponse(1L, "concert1");
        ConcertDto.ConcertResponse concert2 = new ConcertDto.ConcertResponse(2L, "concert2");
        return ResponseEntity.ok(List.of(concert1, concert2));
    }

    /**
     * 콘서트 공연 목록 조회
     */
    @GetMapping("/{concertId}/performances")
    public ResponseEntity<List<ConcertDto.PerformanceResponse>> performances(@RequestHeader("Queue-Token") String token, @PathVariable String concertId) {
        ConcertDto.PerformanceResponse performance1 = ConcertDto.PerformanceResponse.builder()
                .id(1L)
                .date(LocalDate.now())
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(2))
                .build();

        ConcertDto.PerformanceResponse performance2 = ConcertDto.PerformanceResponse.builder()
                .id(2L)
                .date(LocalDate.now().plusDays(1))
                .startAt(LocalDateTime.now().plusDays(1))
                .endAt(LocalDateTime.now().plusDays(1).plusHours(2))
                .build();

        return ResponseEntity.ok(List.of(performance1, performance2));
    }

    /**
     * 콘서트 공연 좌석 목록 조회
     */
    @GetMapping("/{concertId}/performances/{performanceId}/seats")
    public ResponseEntity<List<ConcertDto.SeatResponse>> seats(@RequestHeader("Queue-Token") String token, @PathVariable String performanceId) {
        ConcertDto.SeatResponse seat1 = ConcertDto.SeatResponse.builder()
                .id(1L)
                .grade("BASIC")
                .no(1)
                .price(100000)
                .status("RESERVED")
                .build();
        ConcertDto.SeatResponse seat2 = ConcertDto.SeatResponse.builder()
                .id(2L)
                .grade("BASIC")
                .no(2)
                .status("AVAILABLE")
                .price(100000)
                .build();
        ConcertDto.SeatResponse seat3 = ConcertDto.SeatResponse.builder()
                .id(3L)
                .grade("VIP")
                .no(3)
                .price(140000)
                .status("SOLD")
                .build();
        ConcertDto.SeatResponse seat4 = ConcertDto.SeatResponse.builder()
                .id(4L)
                .grade("VIP")
                .no(4)
                .price(140000)
                .status("AVAILABLE")
                .build();

        return ResponseEntity.ok(List.of(seat1, seat2, seat3, seat4));

    }
}
