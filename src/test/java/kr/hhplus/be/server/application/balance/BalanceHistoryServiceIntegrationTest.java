package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceChangeType;
import kr.hhplus.be.server.domain.balance.BalanceHistory;
import kr.hhplus.be.server.domain.balance.BalanceHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest
@Transactional
class BalanceHistoryServiceIntegrationTest { // 실제로 DB에 저장되었는지 확인

    @Autowired
    BalanceHistoryService service;

    @Autowired
    BalanceHistoryRepository repository;

    @Test
    @DisplayName("recordHistory가 호출되면 DB에 히스토리가 저장된다")
    void recordHistory_persistsToDatabase() {
        RecordBalanceHistoryCommand command = new RecordBalanceHistoryCommand(1L, 5000L, BalanceChangeType.CHARGE,"충전");

        service.recordHistory(command);

        BalanceHistory history = repository.findAllByUserId(1L).get(0);

        assertThat(history.getUserId()).isEqualTo(1L);
        assertThat(history.getAmount()).isEqualTo(5000L);
        assertThat(history.getReason()).isEqualTo("충전");
    }
}
