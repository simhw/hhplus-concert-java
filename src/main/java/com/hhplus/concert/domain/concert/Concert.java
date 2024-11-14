package com.hhplus.concert.domain.concert;

import com.hhplus.concert.domain.BaseTimeEntity;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Builder
@Getter
@Entity
@Table(name = "concert")
@AllArgsConstructor
public class Concert extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    private String title;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<ConcertPerformance> performances = new ArrayList<>();

    protected Concert() {
    }

    public Concert(String title, List<ConcertPerformance> performances) {
        this.title = title;
        setPerformances(performances);
    }

    private void setPerformances(List<ConcertPerformance> performances) {
        if (performances == null || performances.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.performances = performances;
        for (ConcertPerformance performance : performances) {
            performance.setConcert(this);
        }
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void verifyIsAvailable() {
        if (isDeleted()) {
            throw new CoreException(ErrorType.CONCERT_NOT_FOUND, this.id);
        }
    }
}
