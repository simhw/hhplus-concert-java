package com.hhplus.concert.domain.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservedEvent {
    private Long reservationId;
    private Long seatId;
}
