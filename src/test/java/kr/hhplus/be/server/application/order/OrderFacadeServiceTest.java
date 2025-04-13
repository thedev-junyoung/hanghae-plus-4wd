package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.product.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class OrderFacadeServiceTest {

    private ProductService productService;
    private OrderService orderService;
    private OrderEventService orderEventService;

    private OrderFacadeService orderFacadeService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        orderService = mock(OrderService.class);
        orderEventService = mock(OrderEventService.class);

        orderFacadeService = new OrderFacadeService(productService, orderService, orderEventService);
    }

    @Test
    @DisplayName("주문을 생성하고 이벤트를 발행한다")
    void createOrder_success() {
        // given
        Long userId = 1L;
        Long productId = 1001L;
        int quantity = 2;
        int size = 270;
        long price = 5000;

        CreateOrderCommand.OrderItemCommand itemCommand = new CreateOrderCommand.OrderItemCommand(productId, quantity, size);
        CreateOrderCommand command = new CreateOrderCommand(userId, List.of(itemCommand));

        ProductDetailResult productResult = new ProductDetailResult(
            new ProductInfo(productId, "Test Product", price, 10)
        );

        when(productService.getProductDetail(any(GetProductDetailCommand.class))).thenReturn(productResult);
        when(orderService.createOrder(eq(userId), anyList(), any(Money.class)))
                .thenReturn(Order.create("order-id", userId,
                        List.of(OrderItem.of(productId, quantity, size, Money.wons(price))),
                        Money.wons(price * quantity)));

        // when
        OrderResult result = orderFacadeService.createOrder(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.totalAmount()).isEqualTo(price * quantity);
        assertThat(result.items()).hasSize(1);
        assertThat(result.status()).isEqualTo(OrderStatus.CREATED);

        // verify interactions
        verify(productService).getProductDetail(any(GetProductDetailCommand.class));
        verify(productService).decreaseStock(any(DecreaseStockCommand.class));
        verify(orderService).createOrder(eq(userId), anyList(), any(Money.class));
        verify(orderEventService).recordPaymentCompletedEvent(any(Order.class));
    }
}
