package com.hhplus.concert.domain.reservation;

import com.hhplus.concert.interfaces.reservation.ReservationDto;
import lombok.Data;

@Data
public class ReservationCommand {
    private Long userId;
    private String token;
    private Long concertId;
    private Long performanceId;
    private Long seatId;

    public static void toReservationCommand(String token, ReservationDto.ReservationRequest request) {
        ReservationCommand command = new ReservationCommand();
        command.setToken(token);
        command.setUserId(request.getUserId());
        command.setConcertId(request.getConcertId());
        command.setPerformanceId(request.getPerformanceId());
        command.setSeatId(request.getSeatId());
    }
}
