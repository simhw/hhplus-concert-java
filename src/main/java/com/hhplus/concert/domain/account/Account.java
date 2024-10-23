package com.hhplus.concert.domain.account;

import com.hhplus.concert.domain.BaseTimeEntity;
import com.hhplus.concert.domain.support.error.CoreException;
import com.hhplus.concert.domain.support.error.ErrorType;
import com.hhplus.concert.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Entity
@Table(name = "account")
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "account_id")
    private Long id;

    private Long amount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Account() {
    }

    public Account(Long amount, User user) {
        this.amount = amount;
        this.user = user;
    }

    /**
     * 천원 이상 충전 시 잔액을 증액한다.
     */
    public void charge(Integer amount) {
        if (amount < 1000) {
            throw new CoreException(ErrorType.MINIMUM_CHARGE_AMOUNT, amount);
        }
        this.amount += amount;
    }

    /**
     * 계좌 이하의 금액 사용 시 잔액을 감액한다.
     */
    public void use(Integer amount) {
        if (this.amount < amount) {
            throw new CoreException(ErrorType.NOT_ENOUGH_ACCOUNT_AMOUNT, amount);
        }
        this.amount -= amount;
    }
}
