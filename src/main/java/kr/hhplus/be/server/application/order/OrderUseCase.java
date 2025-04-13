package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;

public interface OrderUseCase {
    Order createOrder(Long userId, List<OrderItem> items, Money totalAmount);

    Order getOrderForPayment(String orderId);

    void markConfirmed(Order order);
}
