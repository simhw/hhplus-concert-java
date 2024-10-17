package com.hhplus.concert.application;

import com.hhplus.concert.domain.concert.*;
import com.hhplus.concert.domain.queue.NotActivateQueueException;
import com.hhplus.concert.domain.queue.Queue;
import com.hhplus.concert.infra.concert.ConcertJpaRepository;
import com.hhplus.concert.infra.queue.QueueJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


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
        Queue queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

        Concert concert1 = new Concert("concert1", null);
        Concert concert2 = new Concert("concert1", null);
        concertJpaRepository.save(concert1);
        concertJpaRepository.save(concert2);

        List<Concert> concerts = concertFacade.getActiveConcerts(queue.getToken());
        assertEquals(concerts.size(), 2);
    }

    @DisplayName("활성화 상태가 아닌 토큰 전달 시 콘서트 목록 반환이 불가능하다.")
    @Test
    void 비활성화_토큰_콘서트_조회() {
        Queue queue = new Queue(UUID.randomUUID().toString());
        queueJpaRepository.save(queue);

        Concert concert1 = new Concert("concert1", null);
        Concert concert2 = new Concert("concert1", null);
        concertJpaRepository.save(concert1);
        concertJpaRepository.save(concert2);

        Assertions.assertThrows(NotActivateQueueException.class, () -> concertFacade.getActiveConcerts(queue.getToken()));
    }

    @DisplayName("공연 시작 30분 이전 공연만 반환한다.")
    @Test
    void 공연_목록_조회() {
        Queue queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

        Performance performance1 = new Performance(LocalDate.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(150), null);
        Performance performance2 = new Performance(LocalDate.now(), LocalDateTime.now().plusMinutes(31), LocalDateTime.now().plusMinutes(151), null);
        Concert concert = new Concert("concert", List.of(performance1, performance2));
        concertJpaRepository.save(concert);

        List<Performance> performances = concertFacade.getAvailablePerformances(queue.getToken(), concert.getId());
        assertEquals(performances.size(), 1);
    }

    @DisplayName("공연 시작 30분이 남지않은 공연 좌석 조회 시 오류가 발생한다.")
    @Test
    void 좌석_목록_조회() {
        Queue queue = new Queue(UUID.randomUUID().toString());
        queue.activate(LocalDateTime.now());
        queueJpaRepository.save(queue);

        Seat seat1 = new Seat("BASIC", 1, 100000, SeatStatus.AVAILABLE);
        Seat seat2 = new Seat("VIP", 2, 150000, SeatStatus.RESERVED);
        Performance performance = new Performance(LocalDate.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(150), List.of(seat1, seat2));
        Concert concert = new Concert("concert", List.of(performance));
        concertJpaRepository.save(concert);

        Assertions.assertThrows(NotActivateQueueException.class, () -> concertFacade.getAllSeats(queue.getToken(), concert.getId(), performance.getId()));
    }
}