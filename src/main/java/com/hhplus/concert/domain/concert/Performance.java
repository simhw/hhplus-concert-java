package com.hhplus.concert.domain.concert;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Table(name = "performance")
public class Performance {
    @Id
    @Column(name = "performance_id")
    private Long id;

    @Description("공연일")
    private LocalDate date;

    @Description("시작 시간")
    private LocalDateTime startAt;

    @Description("종료 시간")
    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToMany(mappedBy = "performance")
    private List<Seat> seats = new ArrayList<>();
}
