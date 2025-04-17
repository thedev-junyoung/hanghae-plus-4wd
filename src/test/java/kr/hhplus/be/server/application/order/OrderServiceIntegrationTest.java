package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;


    /**
     * 주문을 생성하고, 영속성 컨텍스트 및 DB에 정상 저장되는지 검증
     * Product ID = 1 (New Balance 993), 수량 2, 가격 10,000 → 총 20,000
     */
    @Test
    @DisplayName("주문을 생성하고 DB에 저장할 수 있다")
    void createOrder_success() {
        Long userId = 1L;
        List<OrderItem> items = List.of(OrderItem.of(1L, 2, 270, Money.wons(10000)));
        Money total = Money.wons(20000);

        Order order = orderService.createOrder(userId, items, total);

        assertThat(orderRepository.findById(order.getId())).isPresent();
    }

    /**
     * 주문 생성 후, CONFIRMED 상태로 전환되는지 확인
     * Product ID = 2 (ASICS GEL-Kayano 14), 가격 169,000
     */
    @Test
    @DisplayName("주문 상태를 CONFIRMED 로 변경할 수 있다")
    void markConfirmed_success() {
        Long userId = 1L;
        Long productId = 2L;
        List<OrderItem> items = List.of(OrderItem.of(productId, 1, 1, Money.wons(169000)));
        Money total = Money.wons(169000);

        Order order = orderService.createOrder(userId, items, total);
        orderService.markConfirmed(order);

        Order confirmed = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(confirmed.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    /**
     * CREATED 상태의 주문을 getOrderForPayment()로 조회할 수 있는지 확인
     * Product ID = 3 (New Balance 530), 가격 12,000
     */
    @Test
    @DisplayName("결제 대상 주문 조회 시 예외 없이 반환된다")
    void getOrderForPayment_success() {
        Long userId = 1L;
        List<OrderItem> items = List.of(OrderItem.of(3L, 1, 280, Money.wons(12000)));
        Money total = Money.wons(12000);

        Order order = orderService.createOrder(userId, items, total);

        Order found = orderService.getOrderForPayment(order.getId());

        assertThat(found.getId()).isEqualTo(order.getId());
    }

    /**
     * 이미 CONFIRMED 상태인 주문을 결제 대상으로 조회할 경우 예외가 발생해야 한다
     * Product ID = 4 (Nike Daybreak), 가격 10,000
     */
    @Test
    @DisplayName("결제 불가 상태면 예외 발생")
    void getOrderForPayment_fail_ifInvalid() {
        Long userId = 1L;
        List<OrderItem> items = List.of(OrderItem.of(4L, 1, 260, Money.wons(10000)));
        Money total = Money.wons(10000);

        Order order = orderService.createOrder(userId, items, total);
        order.markConfirmed();
        orderRepository.save(order);

        assertThatThrownBy(() -> orderService.getOrderForPayment(order.getId()))
                .isInstanceOf(OrderException.InvalidStateException.class);
    }


}
