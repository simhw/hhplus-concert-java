package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "concert")
@AllArgsConstructor
public class Concert extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "concert_id")
    private Long id;

    private String title;

    private LocalDateTime deletedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "concert_id")
    private List<Performance> performances = new ArrayList<>();

    protected Concert() {
    }

    public Concert(String title, List<Performance> performances) {
        this.title = title;
        this.performances = performances;
    }
}
