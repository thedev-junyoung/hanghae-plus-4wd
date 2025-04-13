package kr.hhplus.be.server.domain.balance;

import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface BalanceRepository{
    Balance save(Balance balance);
    Optional<Balance> findByUserId(Long userId);
}