package com.hhplus.concert.domain.account;

import com.hhplus.concert.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountFacadeTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("1,000원 이상 금액 충전 시 계좌 금액이 증액된다.")
    void 금액_충전() {
        // given
        int amount = 1000;
        User user = new User("name", "email");
        Account account = new Account(0L, user);

        when(accountRepository.getAccount(user)).thenReturn(account);

        // when
        Account charged = accountService.charge(user, amount);

        // then
        Assertions.assertThat(charged.getAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("1,000원 미만 금액 충전 시 계좌 금액이 증액되지 않는다.")
    void 최소_금액_미만_충전() {
        // given
        int amount = 900;
        User user = new User("name", "email");
        Account account = new Account(0L, user);

        when(accountRepository.getAccount(user)).thenReturn(account);

        // when
        assertThrows(NotValidAmountException.class, () -> accountService.charge(user, amount));
    }

    @Test
    @DisplayName("잔액보다 낮은 금액 사용 시 계좌 금액이 차감된다.")
    void 금액_사용() {
        // given
        int amount = 1000;
        User user = new User("name", "email");
        Account account = new Account(1000L, user);

        when(accountRepository.getAccount(user)).thenReturn(account);

        // when
        Account charged = accountService.use(user, amount);

        // then
        Assertions.assertThat(charged.getAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("잔액보다 높은 금액 사용 시 오류가 발생한다.")
    void 초과_금액_사용() {
        // given
        int amount = 2000;
        User user = new User("name", "email");
        Account account = new Account(1000L, user);

        when(accountRepository.getAccount(user)).thenReturn(account);

        // when, then
        assertThrows(NotEnoughAccountAmount.class, () -> accountService.use(user, amount));
    }
}