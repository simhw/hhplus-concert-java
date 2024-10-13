package com.hhplus.concert.domain.accunt;

import com.hhplus.concert.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "account_id")
    private Long id;

    private Long amount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Account() {
    }

    public Account(Long amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    public void charge(Integer amount) {
        if (amount < 1000) {
            throw new NotValidAmountException();
        }
        this.amount += amount;
    }
}
