package com.hhplus.concert.domain.reservation;

import com.hhplus.concert.domain.BaseTimeEntity;

import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.user.User;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "reservation")
@AllArgsConstructor
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Description("예약 상태")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Description("결제 금액")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    protected Reservation() {
    }

    public Reservation(ReservationStatus status, int amount, User user, Seat seat) {
        this.status = status;
        this.amount = amount;
        this.user = user;
        this.seat = seat;
    }

    public boolean isExpiredToPay(LocalDateTime expireTime) {
        return getCreatedAt().isBefore(expireTime);
    }

    public void expire() {
        this.status = ReservationStatus.PAYMENT_EXPIRED;
    }

    public void complete() {
        this.status = ReservationStatus.PAYMENT_COMPLETED;
    }
}

