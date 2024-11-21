package com.hhplus.concert.domain.reservation;

import com.hhplus.concert.interfaces.reservation.ReservationDto;
import lombok.Data;

@Data
public class ReservationCommand {
    private Long userId;
    private Long concertId;
    private Long performanceId;
    private Long seatId;

    public static ReservationCommand of(ReservationDto.ReservationRequest request) {
        ReservationCommand command = new ReservationCommand();
        command.setUserId(request.getUserId());
        command.setConcertId(request.getConcertId());
        command.setPerformanceId(request.getPerformanceId());
        command.setSeatId(request.getSeatId());
        return command;
    }
}
