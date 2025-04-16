package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceException;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class BalanceServiceIntegrationTest {

    @Autowired
    BalanceUseCase balanceService;

    @Autowired
    BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        Balance balance = Balance.createNew(null, 1L, Money.wons(10_000));
        balanceRepository.save(balance);
    }

    @Test
    @DisplayName("잔액을 충전할 수 있다")
    void chargeBalance_success() {
        // given
        ChargeBalanceCommand command = new ChargeBalanceCommand(1L, 5_000L);

        // when
        balanceService.charge(command);

        // then: DB 상태 확인
        Balance balance = balanceRepository.findByUserId(1L).orElseThrow();
        assertThat(balance.getAmount()).isEqualTo(15_000L);
    }

    @Test
    @DisplayName("최소 충전 금액 미만이면 예외가 발생한다")
    void chargeBalance_belowMinimum_throwsException() {
        ChargeBalanceCommand command = new ChargeBalanceCommand(1L, 999L);

        assertThatThrownBy(() -> balanceService.charge(command))
                .isInstanceOf(BalanceException.MinimumChargeAmountException.class);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 잔액 충전 시 예외가 발생한다")
    void chargeBalance_userNotFound_throwsException() {
        ChargeBalanceCommand command = new ChargeBalanceCommand(999L, 5_000L);

        assertThatThrownBy(() -> balanceService.charge(command))
                .isInstanceOf(BalanceException.NotFoundException.class);
    }

    @Test
    @DisplayName("잔액을 차감할 수 있다")
    void decreaseBalance_success() {
        DecreaseBalanceCommand command = DecreaseBalanceCommand.of(1L, 5_000L);

        boolean result = balanceService.decreaseBalance(command);

        assertThat(result).isTrue();
        assertThat(balanceRepository.findByUserId(1L).get().getAmount()).isEqualTo(5_000L);
    }

    @Test
    @DisplayName("잔액이 부족하면 예외가 발생한다")
    void decreaseBalance_insufficientFunds_throwsException() {
        DecreaseBalanceCommand command = DecreaseBalanceCommand.of(1L, 100_000L);

        assertThatThrownBy(() -> balanceService.decreaseBalance(command))
                .isInstanceOf(BalanceException.NotEnoughBalanceException.class);
    }

    @Test
    @DisplayName("잔액을 조회할 수 있다")
    void getBalance_success() {
        BalanceResult result = balanceService.getBalance(1L);

        assertThat(result.balance()).isEqualTo(10_000L);
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 예외가 발생한다")
    void getBalance_userNotFound_throwsException() {
        assertThatThrownBy(() -> balanceService.getBalance(999L))
                .isInstanceOf(BalanceException.NotFoundException.class);
    }
}
