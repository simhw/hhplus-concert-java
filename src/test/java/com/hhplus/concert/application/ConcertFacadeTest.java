package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.*;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ConcertFacadeTest {
    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    Concert CONCERT;
    ConcertPerformance PERFORMANCE1, PERFORMANCE2;
    Seat SEAT1, SEAT2, SEAT3, SEAT4;

    @BeforeEach
    void init() {
        LocalDateTime now = LocalDateTime.now();
        SEAT1 = Seat.builder()
                .grade("BASIC").no(1).price(50000).status(SeatStatus.AVAILABLE)
                .build();
        SEAT2 = Seat.builder()
                .grade("VIP").no(2).price(100000).status(SeatStatus.AVAILABLE)
                .build();
        SEAT3 = Seat.builder()
                .grade("BASIC").no(1).price(50000).status(SeatStatus.AVAILABLE)
                .build();
        SEAT4 = Seat.builder()
                .grade("VIP").no(2).price(100000).status(SeatStatus.AVAILABLE)
                .build();

        PERFORMANCE1 = new ConcertPerformance(LocalDate.now(), now.plusMinutes(30), now.plusMinutes(180), List.of(SEAT1, SEAT2));
        PERFORMANCE2 = new ConcertPerformance(LocalDate.now(), now.plusMinutes(200), now.plusMinutes(350), List.of(SEAT3, SEAT4));

        CONCERT = new Concert("CONCERT", List.of(PERFORMANCE1, PERFORMANCE2));
        concertJpaRepository.save(CONCERT);
    }

    @DisplayName("콘서트 목록을 반환한다.")
    @Test
    void 콘서트_조회() {
        // when
        List<ConcertInfo> concerts = concertFacade.getConcertInfos();

        // then
        assertThat(concerts.size()).isEqualTo(1);
    }

    @DisplayName("공연 시작 30분 이전 공연만 반환한다.")
    @Test
    void 공연_목록_조회() {
        // when
        List<ConcertPerformanceInfo> performances = concertFacade.getAvailablePerformanceInfos(CONCERT.getId());
        // then
        assertThat(performances.size()).isEqualTo(1);
    }

    @DisplayName("공연 시작 30분 이전 공연 좌석 조회 시 'PERFORMANCE_EXPIRED' 예외가 발생한다.")
    @Test
    void 좌석_목록_조회() {
        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class,
                () -> concertFacade.getSeatInfos(CONCERT.getId(), PERFORMANCE1.getId()));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PERFORMANCE_EXPIRED);
    }
}