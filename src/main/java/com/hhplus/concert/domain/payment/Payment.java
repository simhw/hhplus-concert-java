package com.hhplus.concert.domain.payment;

import com.hhplus.concert.domain.BaseTimeEntity;
import com.hhplus.concert.domain.reservation.Reservation;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Getter;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "payment")
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Description("총 결제 금액")
    private Integer amount;

    @OneToOne
    private Reservation reservation;
    protected Payment() {
    }

    public Payment(Integer amount, Reservation reservation) {
        this.amount = amount;
        this.reservation = reservation;
    }
}
