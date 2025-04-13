package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceUseCase;
import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.application.order.OrderUseCase;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacadeService {
    private final PaymentUseCase paymentUseCase;
    private final OrderUseCase orderUseCase;
    private final BalanceUseCase balanceUseCase;

    // PaymentFacadeService.java
    @Transactional
    public PaymentResult requestPayment(RequestPaymentCommand command) {
        Money amount = Money.wons(command.amount());

        Order order = orderUseCase.getOrderForPayment(command.orderId());

        balanceUseCase.decreaseBalance(
                new DecreaseBalanceCommand(command.userId(), amount.value())
        );

        Payment payment = paymentUseCase.recordSuccess(
                PaymentCommand.from(command)
        );

        orderUseCase.markConfirmed(order);

        return PaymentResult.from(payment);
    }

}
