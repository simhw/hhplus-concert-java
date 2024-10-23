package com.hhplus.concert.domain.concert;

import lombok.Data;

@Data
public class SeatInfo {
    private Long id;
    private String grade;
    private Integer no;
    private Integer price;
    private String status;

    public static SeatInfo toSeatInfo(Seat seat) {
        SeatInfo info = new SeatInfo();
        info.setId(seat.getId());
        info.setGrade(seat.getGrade());
        info.setNo(seat.getNo());
        info.setPrice(seat.getPrice());
        info.setStatus(seat.getStatus().toString());
        return info;
    }
}
