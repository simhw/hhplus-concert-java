package com.hhplus.concert.interfaces.concert;

import com.hhplus.concert.application.ConcertFacade;
import com.hhplus.concert.domain.concert.ConcertInfo;
import com.hhplus.concert.domain.concert.ConcertPerformanceInfo;
import com.hhplus.concert.domain.concert.SeatInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Concert", description = "콘서트 API")
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    /**
     * 콘서트 목록 조회
     */
    @GetMapping("")
    public ResponseEntity<List<ConcertDto.ConcertResponse>> concerts() {
        List<ConcertInfo> infos = concertFacade.getConcertInfos();
        List<ConcertDto.ConcertResponse> responses = infos.stream()
                .map(v -> ConcertDto.ConcertResponse.builder()
                        .id(v.getId())
                        .title(v.getTitle())
                        .build())
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * 콘서트 공연 목록 조회
     */
    @GetMapping("/{concertId}/performances")
    public ResponseEntity<List<ConcertDto.PerformanceResponse>> performances(
            @PathVariable Long concertId
    ) {
        List<ConcertPerformanceInfo> infos = concertFacade.getAvailablePerformanceInfos(concertId);
        List<ConcertDto.PerformanceResponse> responses = infos.stream()
                .map(v -> ConcertDto.PerformanceResponse.builder()
                        .id(v.getId())
                        .date(v.getDate())
                        .startAt(v.getStartAt())
                        .endAt(v.getEndAt())
                        .build())
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * 콘서트 공연 좌석 목록 조회
     */
    @GetMapping("/{concertId}/performances/{performanceId}/seats")
    public ResponseEntity<List<ConcertDto.SeatResponse>> seats(
            @PathVariable Long concertId,
            @PathVariable Long performanceId
    ) {
        List<SeatInfo> infos = concertFacade.getSeatInfos(concertId, performanceId);
        List<ConcertDto.SeatResponse> responses = infos.stream()
                .map(v -> ConcertDto.SeatResponse.builder()
                        .id(v.getId())
                        .grade(v.getGrade())
                        .no(v.getNo())
                        .price(v.getPrice())
                        .status(v.getStatus())
                        .build()
                )
                .toList();
        return ResponseEntity.ok(responses);
    }
}
