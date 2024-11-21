package com.hhplus.concert.domain.reservation;

import lombok.Data;

@Data
public class ReservationInfo {
    private Long id;
    private String status;
    private Integer amount;

    public static ReservationInfo from(Reservation reservation) {
        ReservationInfo info = new ReservationInfo();
        info.setId(reservation.getId());
        info.setStatus(reservation.getStatus().toString());
        info.setAmount(reservation.getAmount());
        return info;
    }
}
