package kr.hhplus.be.server.infrastructure.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class BalanceRepositoryImpl implements BalanceRepository {
    @Override
    public Balance save(Balance balance) {
        return null;
    }

    @Override
    public Optional<Balance> findByUserId(Long userId) {
        return Optional.empty();
    }
}
