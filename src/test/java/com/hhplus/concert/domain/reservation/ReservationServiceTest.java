package com.hhplus.concert.domain.reservation;

import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.concert.SeatStatus;
import com.hhplus.concert.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    void 좌석_예약() {
        // given
        User user = new User(1L, "user", "email", null);
        Seat seat = new Seat(1L, "VIP", 1, 100000, SeatStatus.RESERVED);

        Reservation reservation = new Reservation(ReservationStatus.PAYMENT_WAITING, seat.getPrice(), user, seat);
        when(reservationRepository.saveReservation(any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation reserved = reservationService.reserve(user, seat);

        // then
        assertNotNull(reserved);
        verify(reservationRepository, times(1)).saveReservation(any(Reservation.class));
    }

}