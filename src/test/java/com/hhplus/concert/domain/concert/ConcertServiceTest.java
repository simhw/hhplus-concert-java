package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    Concert CONCERT;
    ConcertPerformance PERFORMANCE1, PERFORMANCE2;
    Seat SEAT1, SEAT2, SEAT3, SEAT4;

    @BeforeEach
    void init() {
        LocalDateTime now = LocalDateTime.now();
        SEAT1 = Seat.builder()
                .id(1L).grade("BASIC").no(1).price(50000).status(SeatStatus.AVAILABLE)
                .build();
        SEAT2 = Seat.builder()
                .id(2L).grade("VIP").no(2).price(100000).status(SeatStatus.AVAILABLE)
                .build();
        SEAT3 = Seat.builder()
                .id(3L).grade("BASIC").no(1).price(50000).status(SeatStatus.AVAILABLE)
                .build();
        SEAT4 = Seat.builder()
                .id(4L).grade("VIP").no(2).price(100000).status(SeatStatus.AVAILABLE)
                .build();
        PERFORMANCE1 = ConcertPerformance.builder()
                .id(1L).date(LocalDate.now()).startAt(now.plusMinutes(30)).endAt(now.plusMinutes(180)).seats(List.of(SEAT1, SEAT2))
                .build();
        PERFORMANCE2 = ConcertPerformance.builder()
                .id(2L).date(LocalDate.now()).startAt(now.plusMinutes(200)).endAt(now.plusMinutes(350)).seats(List.of(SEAT3, SEAT4))
                .build();
        CONCERT = Concert.builder()
                .id(1L).title("CONCERT").performances(List.of(PERFORMANCE1, PERFORMANCE2))
                .build();
    }

    @Test
    void 콘서트_목록_조회() {
        // when
        concertService.getConcertInfos();
        // then
        verify(concertRepository, times(1)).getConcerts();
    }

    @DisplayName("시작 시간 30분 전 공연 목록을 반환한다.")
    @Test
    void 콘서트_공연_목록_조회() {
        // given
        when(concertRepository.getConcert(1L)).thenReturn(CONCERT);

        // when
        List<ConcertPerformanceInfo> performances = concertService.getAvailablePerformanceInfos(1L);

        // then
        assertThat(performances).hasSize(1);
    }

    @DisplayName("시작 시간 30분 전 공연의 좌석을 반환한다.")
    @Test
    void 공연_좌석_목록_조회() {
        // given
        when(concertRepository.getConcert(1L)).thenReturn(CONCERT);

        // when
        List<SeatInfo> seats = concertService.getSeatInfos(1L, 2L);

        // then
        assertThat(seats).hasSize(2);
    }

    @DisplayName("시작 시간 30분이 남지 않은 공연은 좌석을 반환하지 않는다.")
    @Test
    void 예약_불가_공연_좌석_목록_조회() {
        // given
        when(concertRepository.getConcert(1L)).thenReturn(CONCERT);

        // when, then
        CoreException exception = Assertions.assertThrows(CoreException.class, () -> concertService.getSeatInfos(1L, 1L));
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.PERFORMANCE_EXPIRED);
    }
}