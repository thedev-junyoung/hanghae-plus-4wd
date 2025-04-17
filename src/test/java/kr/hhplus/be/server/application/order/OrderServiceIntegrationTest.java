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

@SpringBootTest
@Transactional
class OrderServiceIntegrationTest { // DB 반영이 중요한 흐름만 최소한으로 테스트

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    /*
     * [테스트 범위 조정]
     * 주문 생성 흐름은 OrderFacadeServiceIntegrationTest에서 전체 흐름을 검증함.
     * 따라서 본 테스트에서는 OrderService의 고유 책임인 상태 변경(markConfirmed) DB 반영 여부만 검증.
     */
//    주문을 생성하고, 영속성 컨텍스트 및 DB에 정상 저장되는지 검증
//    Product ID = 1 (New Balance 993), 수량 2, 가격 10,000 → 총 20,000
//    @Test
//    @DisplayName("주문을 생성하고 DB에 저장할 수 있다")
//    void createOrder_success() {
//        Long userId = 1L;
//        List<OrderItem> items = List.of(OrderItem.of(1L, 2, 270, Money.wons(10000)));
//        Money total = Money.wons(20000);
//
//        Order order = orderService.createOrder(userId, items, total);
//
//        assertThat(orderRepository.findById(order.getId())).isPresent();
//    }

    // 주문 생성 후, CONFIRMED 상태로 전환되는지 확인
    // Product ID = 2 (ASICS GEL-Kayano 14), 가격 169,000
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






}
