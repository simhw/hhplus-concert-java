package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.concert.exception.ExpiredPerformanceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    void 콘서트_목록_조회() {
        // when
        List<Concert> concerts = concertService.getActiveConcerts();
        //
        verify(concertRepository, times(1)).getConcerts();
    }

    @DisplayName("시작 시간 30분 전 공연 목록을 반환한다.")
    @Test
    void 콘서트_공연_목록_조회() {
        // given
        Performance performance1 = new Performance(1L, LocalDate.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(150), null);
        Performance performance2 = new Performance(2L, LocalDate.now(), LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(8), null);
        Concert concert = new Concert(1L, "concert", null, List.of(performance1, performance2));
        when(concertRepository.getConcert(1L)).thenReturn(concert);

        // when
        List<Performance> performances = concertService.getAvailablePerformances(1L);

        // then
        assertThat(performances).hasSize(1);
    }

    @DisplayName("시작 시간 30분 전 공연의 좌석을 반환한다.")
    @Test
    void 공연_좌석_목록_조회() {
        // given
        Seat seat1 = new Seat(1L, "VIP", 1, 100000, SeatStatus.AVAILABLE);
        Seat seat2 = new Seat(2L, "BASIC", 2, 80000, SeatStatus.AVAILABLE);
        Performance performance = new Performance(1L, LocalDate.now(), LocalDateTime.now().plusMinutes(60), LocalDateTime.now().plusMinutes(180), List.of(seat1, seat2));
        Concert concert = new Concert(1L, "concert", null, List.of(performance));
        when(concertRepository.getConcert(1L)).thenReturn(concert);

        // when
        List<Seat> seats = concertService.getAllSeats(1L, 1L);

        // then
        assertThat(seats).hasSize(2);
    }

    @DisplayName("시작 시간 30분이 남지 않은 공연은 좌석을 반환하지 않는다.")
    @Test
    void 예약_불가_공연_좌석_목록_조회() {
        // given
        Seat seat1 = new Seat(1L, "VIP", 1, 100000, SeatStatus.AVAILABLE);
        Seat seat2 = new Seat(2L, "BASIC", 2, 80000, SeatStatus.AVAILABLE);
        Performance performance = new Performance(1L, LocalDate.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusMinutes(150), List.of(seat1, seat2));
        Concert concert = new Concert(1L, "concert", null, List.of(performance));
        when(concertRepository.getConcert(1L)).thenReturn(concert);

        // when, then
        Assertions.assertThrows(ExpiredPerformanceException.class, () -> concertService.getAllSeats(1L, 1L));
    }
}