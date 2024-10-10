package com.hhplus.concert.domain.reservation;

import com.hhplus.concert.domain.concert.Concert;
import com.hhplus.concert.domain.concert.Performance;
import com.hhplus.concert.domain.concert.Seat;
import com.hhplus.concert.domain.user.User;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_status")
    @Description("예약 상태")
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToOne
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
}
