package com.hhplus.concert.domain.concert;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "concert")
public class Concert {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @OneToMany(mappedBy = "concert")
    private List<Performance> performances = new ArrayList<>();

}
