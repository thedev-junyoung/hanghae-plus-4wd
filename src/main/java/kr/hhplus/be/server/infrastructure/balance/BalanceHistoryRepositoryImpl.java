package kr.hhplus.be.server.infrastructure.balance;

import kr.hhplus.be.server.domain.balance.BalanceHistory;
import kr.hhplus.be.server.domain.balance.BalanceHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BalanceHistoryRepositoryImpl implements BalanceHistoryRepository {
    @Override
    public void save(BalanceHistory history) {

    }
}
