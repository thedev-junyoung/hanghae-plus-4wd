package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderFacadeServiceIntegrationTest {

    @Autowired
    OrderFacadeService orderFacadeService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    @Test
    @DisplayName("쿠폰 없이 단일 상품 주문이 성공한다")
    void createOrder_withoutCoupon_success() {
        // given
        Long userId = 1L;
        Long productId = 1L; // New Balance 993
        int quantity = 2;
        int size = 270;
        int price = 199000;

        CreateOrderCommand command = CreateOrderCommand.of(
                userId,
                List.of(new CreateOrderCommand.OrderItemCommand(productId, quantity, size)),
                null
        );

        // when
        OrderResult result = orderFacadeService.createOrder(command);

        // then
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.totalAmount()).isEqualTo(price * quantity);
        assertThat(orderRepository.findById(result.orderId())).isPresent();
    }

    @Test
    @DisplayName("쿠폰을 적용한 주문이 성공한다")
    void createOrder_withCoupon_success() {
        // given
        Long userId = 1L;
        String couponCode = "LIMITED10";
        Long productId = 2L; // ASICS GEL-Kayano 14
        int price = 169000;
        int discount = 10000;

        Coupon coupon = Coupon.create(couponCode, CouponType.FIXED, discount, 10,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        couponRepository.save(coupon);
        couponIssueRepository.save(CouponIssue.create(userId, coupon));

        CreateOrderCommand command = CreateOrderCommand.of(
                userId,
                List.of(new CreateOrderCommand.OrderItemCommand(productId, 1, 265)),
                couponCode
        );

        // when
        OrderResult result = orderFacadeService.createOrder(command);

        // then
        assertThat(result.totalAmount()).isEqualTo(price - discount);
        assertThat(orderRepository.findById(result.orderId())).isPresent();
    }

    @Test
    @DisplayName("이미 사용된 쿠폰을 적용하면 예외가 발생한다")
    void createOrder_fail_ifCouponUsed() {
        // given
        Long userId = 1L;
        String couponCode = "USED_COUPON";
        Long productId = 3L;

        Coupon coupon = Coupon.create(couponCode, CouponType.FIXED, 5000, 10,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        couponRepository.save(coupon);

        CouponIssue issue = CouponIssue.create(userId, coupon);
        issue.markAsUsed();
        couponIssueRepository.save(issue);

        CreateOrderCommand command = CreateOrderCommand.of(
                userId,
                List.of(new CreateOrderCommand.OrderItemCommand(productId, 1, 270)),
                couponCode
        );

        // when & then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(CouponException.AlreadyIssuedException.class);
    }
}
