package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.BaseTimeEntity;
import com.hhplus.concert.domain.concert.exception.ExpiredPerformanceException;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "performance")
@AllArgsConstructor
public class Performance extends BaseTimeEntity {
    @Id
    @Column(name = "performance_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Description("공연일")
    private LocalDate date;

    @Description("시작 시간")
    private LocalDateTime startAt;

    @Description("종료 시간")
    private LocalDateTime endAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "performance_id")
    private List<Seat> seats = new ArrayList<>();

    protected Performance() {
    }

    public Performance(LocalDate date, LocalDateTime startAt, LocalDateTime endAt, List<Seat> seats) {
        this.date = date;
        this.startAt = startAt;
        this.endAt = endAt;
        this.seats = seats;
    }

    public void verifyIsNotExpired(LocalDateTime now) {
        if (!isNotExpired(now)) {
            throw new ExpiredPerformanceException();
        }
    }

    /**
     * 공연 시작 30분 전까지 예약 가능
     */
    public boolean isNotExpired(LocalDateTime now) {
        return now.isBefore(startAt.minusMinutes(30));
    }
}
