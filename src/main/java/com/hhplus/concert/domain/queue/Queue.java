package com.hhplus.concert.domain.queue;

import com.hhplus.concert.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Getter
@Table
@Entity(name = "queue")
@AllArgsConstructor
public class Queue extends BaseTimeEntity {
    @Description("아이디 및 대기 번호")
    @Id
    @Column(name = "queue_id")
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Description("대기 상태")
    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    @Description("외부 식별자")
    @Column(unique = true, nullable = false)
    private String token;

    @Description("활성화 전환 시간")
    private LocalDateTime enteredAt;

    protected Queue() {
    }

    public Queue(String token) {
        this.token = token;
        this.status = QueueStatus.WAITING;
    }

    public void activate(LocalDateTime enteredAt) {
        this.enteredAt = enteredAt;
        this.status = QueueStatus.ACTIVE;
    }

    public void expire() {
        this.status = QueueStatus.EXPIRED;
    }

    public void verifyIsActive(int expiredTimeSeconds) {
        if (isWaiting() || isExpired(expiredTimeSeconds)) {
            throw new NotActivateQueueException();
        }
    }

    public boolean isWaiting() {
        return status.equals(QueueStatus.WAITING);
    }

    public boolean isExpired(int expiredTimeSeconds) {
        return status.equals(QueueStatus.EXPIRED)
                || (enteredAt != null && LocalDateTime.now().isAfter(enteredAt.plusSeconds(expiredTimeSeconds)));
    }
}
