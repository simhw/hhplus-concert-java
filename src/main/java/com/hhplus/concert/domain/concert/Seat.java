package com.hhplus.concert.domain.concert;

import jakarta.persistence.*;
import jdk.jfr.Description;

import static jakarta.persistence.GenerationType.*;

@Table
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "seat_id")
    private Long id;

    @Description("등급")
    private String grade;

    @Description("좌석 번호")
    private Integer no;

    @Description("가격")
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performance;
}
