package com.hhplus.concert.domain.queue;

import jakarta.persistence.*;
import jdk.jfr.Description;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Table
@Entity(name = "queue")
public class Queue {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Description("아이디 및 대기 번호")
    private Long id;

    private String status;  //

    @Description("외부 식별자")
    private String code;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Description("활성화된 시간")
    private LocalDateTime enteredAt;
}
