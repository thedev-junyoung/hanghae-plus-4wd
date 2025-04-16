package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceHistory;
import kr.hhplus.be.server.domain.balance.BalanceHistoryRepository;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BalanceFacadeIntegrationTest {

    @Autowired
    private BalanceFacade balanceFacade;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceHistoryRepository balanceHistoryRepository;

    @Test
    @DisplayName("잔액 충전 성공 - 기존 잔액 + 충전 금액")
    void charge_success() {
        // given
        Long userId = 1L;
        Money initial = Money.wons(10_000);
        Money charge = Money.wons(5_000);
        Balance existing = Balance.createNew(null, userId, initial);
        balanceRepository.save(existing);

        ChargeBalanceCriteria criteria = ChargeBalanceCriteria.of(userId, charge.value(), "테스트 충전");

        // when
        balanceFacade.charge(criteria);

        // then: DB 기준 검증
        Balance updated = balanceRepository.findByUserId(userId).orElseThrow();
        assertThat(Money.wons(updated.getAmount())).isEqualTo(initial.add(charge));

        BalanceHistory history = balanceHistoryRepository.findAllByUserId(userId).get(0);
        assertThat(Money.wons(history.getAmount())).isEqualTo(charge);
        assertThat(history.isChargeHistory()).isTrue();
        assertThat(history.getReason()).isEqualTo("테스트 충전");
    }
}
