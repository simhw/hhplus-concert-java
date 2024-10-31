package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Builder
@Getter
@Entity
@AllArgsConstructor
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "seat_id")
    private Long id;

    @Version
    private Long version;

    @Description("등급")
    private String grade;

    @Description("좌석 번호")
    @Column(name = "seat_no")
    private Integer no;

    @Description("가격")
    private Integer price;

    @Description("좌석 상태")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "concert_performance_id")
    private ConcertPerformance performance;

    protected Seat() {
    }

    public Seat(String grade, Integer no, Integer price, SeatStatus status) {
        this.grade = grade;
        this.no = no;
        this.price = price;
        this.status = status;
    }

    public void verifyIsAvailable() {
        if (!status.equals(SeatStatus.AVAILABLE)) {
            throw new CoreException(ErrorType.DUPLICATED_RESERVATION, this.id);
        }
    }

    public void occupy() {
        verifyIsAvailable();
        this.status = SeatStatus.RESERVED;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
    }
}
