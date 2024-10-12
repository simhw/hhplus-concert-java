package com.hhplus.concert.domain;

import com.hhplus.concert.domain.reservation.Reservation;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Description("총 결제 금액")
    private Long amount;

    @OneToOne
    private Reservation reservation;

    @Description("결제 시간")
    private LocalDateTime createdAt;
}
