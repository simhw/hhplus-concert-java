package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.*;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import org.junit.jupiter.api.Assertions;
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

    @DisplayName("콘서트 목록을 반환한다.")
    @Test
    void 콘서트_조회() {
        for (int i = 1; i <= 3; i++) {
            Concert concert = new Concert("concert" + i, null);
            concertJpaRepository.save(concert);
        }

        // when
        List<ConcertInfo> concerts = concertFacade.getConcertInfos();

        // then
        assertThat(concerts.size()).isEqualTo(3);
    }

    @DisplayName("공연 시작 30분 이전 공연만 반환한다.")
    @Test
    void 공연_목록_조회() {
        ConcertPerformance performance1 = new ConcertPerformance(
                LocalDate.now(),
                LocalDateTime.now().plusMinutes(30),
                LocalDateTime.now().plusMinutes(150),
                null
        );
        ConcertPerformance performance2 = new ConcertPerformance(
                LocalDate.now(),
                LocalDateTime.now().plusMinutes(31),
                LocalDateTime.now().plusMinutes(151),
                null
        );
        Concert concert = new Concert("concert", List.of(performance1, performance2));
        concertJpaRepository.save(concert);

        List<ConcertPerformanceInfo> performances = concertFacade.getAvailablePerformanceInfos(concert.getId());
        assertThat(performances.size()).isEqualTo(1);
    }

    @DisplayName("공연 시작 30분 이전 공연 좌석 조회 시 'PERFORMANCE_EXPIRED' 예외가 발생한다.")
    @Test
    void 좌석_목록_조회() {
        // given
        Seat seat1 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        Seat seat2 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);
        ConcertPerformance performance = new ConcertPerformance(
                LocalDate.now(),
                LocalDateTime.now().plusMinutes(30),
                LocalDateTime.now().plusMinutes(150),
                List.of(seat1, seat2)
        );
        Concert concert = new Concert("concert", List.of(performance));
        concertJpaRepository.save(concert);

        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class,
                () -> concertFacade.getSeatInfos(concert.getId(), performance.getId()));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PERFORMANCE_EXPIRED);
    }
}