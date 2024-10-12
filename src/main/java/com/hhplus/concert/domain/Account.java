package com.hhplus.concert.domain;

import com.hhplus.concert.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private Long amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
