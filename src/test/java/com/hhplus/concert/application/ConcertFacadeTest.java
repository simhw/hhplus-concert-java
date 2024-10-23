package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.*;
import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.queue.QueueJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ConcertFacadeTest {

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private QueueJpaRepository queueJpaRepository;

    @DisplayName("활성화 토큰 전달 시 콘서트 목록을 반환한다.")
    @Test
    void 콘서트_조회() {
        // given
        Queue queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

        for (int i = 1; i <= 3; i++) {
            Concert concert = new Concert("concert" + i, null);
            concertJpaRepository.save(concert);
        }

        // when
        List<ConcertInfo> concerts = concertFacade.getConcertInfos(queue.getToken());

        // then
        assertThat(concerts.size()).isEqualTo(3);
    }

    @DisplayName("활성화 상태가 아닌 토큰 전달 시 콘서트 목록 반환이 불가능하다.")
    @Test
    void 비활성화_토큰_콘서트_조회() {
        // given
        Queue queue = new Queue(UUID.randomUUID().toString());
        queueJpaRepository.save(queue);

        for (int i = 1; i <= 3; i++) {
            Concert concert = new Concert("concert" + i, null);
            concertJpaRepository.save(concert);
        }

        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class,
                () -> concertFacade.getConcertInfos(queue.getToken()));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_ACTIVE_QUEUE);
    }

    @DisplayName("공연 시작 30분 이전 공연만 반환한다.")
    @Test
    void 공연_목록_조회() {
        Queue queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

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

        List<ConcertPerformanceInfo> performances = concertFacade.getAvailablePerformanceInfos(queue.getToken(), concert.getId());
        assertThat(performances.size()).isEqualTo(1);
    }

    @DisplayName("공연 시작 30분 이전 공연 좌석 조회 시 'PERFORMANCE_EXPIRED' 예외가 발생한다.")
    @Test
    void 좌석_목록_조회() {
        // given
        Queue queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

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
                () -> concertFacade.getSeatInfos(queue.getToken(), concert.getId(), performance.getId()));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PERFORMANCE_EXPIRED);
    }
}