package com.hhplus.concert.domain.concert;

import lombok.Data;

@Data
public class ConcertInfo {
    private Long id;
    private String title;

    public static ConcertInfo toConcertInfo(Concert concert) {
        ConcertInfo info = new ConcertInfo();
        info.setId(concert.getId());
        info.setTitle(concert.getTitle());
        return info;
    }
}
