package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceUseCase;
import kr.hhplus.be.server.application.order.OrderUseCase;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class PaymentFacadeServiceIntegrationTest {

    @Autowired
    PaymentFacadeService paymentFacadeService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("잔액 차감 → 결제 기록 → 주문 상태 변경까지 전체 흐름을 검증한다")
    void requestPayment_shouldDeductBalance_RecordPayment_AndConfirmOrder() {
        // given
        Long userId = 1L;
        String orderId = "ORDER-001";
        long amount = 10000L;
        String method = "BALANCE";

        Balance balance = Balance.createNew(null, userId, Money.wons(20000L));
        balanceRepository.save(balance);

        Order order = Order.create(userId, createFakeItems(), Money.wons(amount));
        orderRepository.save(order);

        RequestPaymentCommand command = new RequestPaymentCommand(order.getId(), userId, amount, method);

        // when
        PaymentResult result = paymentFacadeService.requestPayment(command);

        // then
        Balance updatedBalance = balanceRepository.findByUserId(userId).orElseThrow();
        assertThat(updatedBalance.getAmount()).isEqualTo(10000L); // 20000 - 10000

        Payment payment = paymentRepository.findByOrderId(order.getId()).orElseThrow();
        assertThat(payment.getAmount()).isEqualTo(10000L);
        assertThat(payment.getMethod()).isEqualTo("BALANCE");

        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.CONFIRMED);

        assertThat(result.orderId()).isEqualTo(order.getId());
        assertThat(result.amount()).isEqualTo(amount);
        assertThat(result.status()).isEqualTo("SUCCESS");
    }
    @Test
    @DisplayName("잔액 부족 시 결제 요청은 실패해야 한다")
    void requestPayment_fail_ifNotEnoughBalance() {
        // given
        Long userId = 1L;
        long balanceAmount = 5000L;
        long paymentAmount = 10000L;
        String orderId = "ORDER-002";
        String method = "BALANCE";

        // 1. 잔액 5,000원 등록
        balanceRepository.save(Balance.createNew(null, userId, Money.wons(balanceAmount)));

        // 2. 주문 금액은 10,000원
        Order order = Order.create(userId, createFakeItems(), Money.wons(paymentAmount));
        orderRepository.save(order);

        RequestPaymentCommand command = new RequestPaymentCommand(order.getId(), userId, paymentAmount, method);

        // when & then
        assertThatThrownBy(() -> paymentFacadeService.requestPayment(command))
                .isInstanceOf(RuntimeException.class) // 실제 예외 클래스에 맞게 변경
                .hasMessageContaining("잔액이 부족");  // 실제 메시지 or 커스텀 예외 사용

        // 상태 변화 없음을 검증해도 좋음 (선택)
        Balance balance = balanceRepository.findByUserId(userId).orElseThrow();
        assertThat(balance.getAmount()).isEqualTo(balanceAmount);

        assertThat(paymentRepository.findByOrderId(order.getId())).isEmpty();

        Order unchanged = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(unchanged.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    private static java.util.List<kr.hhplus.be.server.domain.order.OrderItem> createFakeItems() {
        return java.util.List.of(
                kr.hhplus.be.server.domain.order.OrderItem.of(1L, 1, 270, Money.wons(10000L))
        );
    }
}
