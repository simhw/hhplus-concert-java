package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.BaseTimeEntity;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Builder
@Getter
@Entity
@Table(name = "concert_performance")
@AllArgsConstructor
public class ConcertPerformance extends BaseTimeEntity {
    @Id
    @Column(name = "concert_performance_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Description("공연일")
    private LocalDate date;

    @Description("시작 시간")
    private LocalDateTime startAt;

    @Description("종료 시간")
    private LocalDateTime endAt;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    protected ConcertPerformance() {
    }

    public ConcertPerformance(LocalDate date, LocalDateTime startAt, LocalDateTime endAt, List<Seat> seats) {
        this.date = date;
        this.startAt = startAt;
        this.endAt = endAt;
        setSeats(seats);
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
        if (seats == null) {
            return;
        }
        for (Seat seat : seats) {
            seat.setPerformance(this);
        }
    }

    public void verifyIsNotExpired(LocalDateTime now) {
        if (!isNotExpired(now)) {
            throw new CoreException(ErrorType.PERFORMANCE_EXPIRED, this.getId());
        }
    }

    /**
     * 공연 시작 30분 전까지 예약 가능하다.
     */
    public boolean isNotExpired(LocalDateTime now) {
        return now.isBefore(startAt.minusMinutes(30));
    }
}
