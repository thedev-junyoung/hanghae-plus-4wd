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
    @DisplayName("잔액을 충전하면 DB에도 반영된다")
    void chargeBalance_success() {
        balanceService.charge(new ChargeBalanceCommand(1L, 5_000L));
        assertThat(balanceRepository.findByUserId(1L).get().getAmount()).isEqualTo(15_000L);
    }

    @Test
    @DisplayName("존재하지 않는 유저는 충전할 수 없다")
    void chargeBalance_userNotFound() {
        assertThatThrownBy(() -> balanceService.charge(new ChargeBalanceCommand(999L, 5000L)))
                .isInstanceOf(BalanceException.NotFoundException.class);
    }

    @Test
    @DisplayName("잔액 차감 성공 시 DB에 반영된다")
    void decreaseBalance_success() {
        balanceService.decreaseBalance(new DecreaseBalanceCommand(1L, 5000L));
        assertThat(balanceRepository.findByUserId(1L).get().getAmount()).isEqualTo(5_000L);
    }

    @Test
    @DisplayName("존재하지 않는 유저는 조회할 수 없다")
    void getBalance_userNotFound() {
        assertThatThrownBy(() -> balanceService.getBalance(999L))
                .isInstanceOf(BalanceException.NotFoundException.class);
    }

    @Test
    @DisplayName("잔액을 조회할 수 있다")
    void getBalance_success() {
        BalanceResult result = balanceService.getBalance(1L);
        assertThat(result.balance()).isEqualTo(10_000L);
    }
}

