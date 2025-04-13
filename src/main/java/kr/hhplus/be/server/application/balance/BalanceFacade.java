package kr.hhplus.be.server.application.balance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceUseCase balanceUseCase;
    private final BalanceHistoryUseCase historyUseCase;

    public BalanceResult charge(ChargeBalanceCriteria criteria) {
        ChargeBalanceCommand command = ChargeBalanceCommand.from(criteria);

        BalanceInfo info = balanceUseCase.charge(command);

        historyUseCase.recordHistory(
                RecordBalanceHistoryCommand.of(criteria)
        );

        return BalanceResult.fromInfo(info);
    }


}

