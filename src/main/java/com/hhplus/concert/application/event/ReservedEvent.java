package com.hhplus.concert.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservedEvent {
    private Long reservationId;
    private Long seatId;
}
