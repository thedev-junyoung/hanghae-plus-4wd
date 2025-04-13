package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacadeService {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final BalanceService balanceService;

    // PaymentFacadeService.java
    @Transactional
    public PaymentResult requestPayment(RequestPaymentCommand command) {
        Money amount = Money.wons(command.amount());

        Order order = orderService.getOrderForPayment(command.orderId());

        balanceService.decreaseBalance(
                new DecreaseBalanceCommand(command.userId(), amount.value())
        );

        Payment payment = paymentService.recordSuccess(
                PaymentCommand.from(command)
        );

        orderService.markConfirmed(order);

        return PaymentResult.from(payment);
    }

}
