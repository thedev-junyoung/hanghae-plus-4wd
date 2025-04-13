package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.product.DecreaseStockCommand;
import kr.hhplus.be.server.application.product.GetProductDetailCommand;

import java.util.List;

public record CreateOrderCommand(
        Long userId,
        List<OrderItemCommand> items,
        String couponCode
) {
    public boolean hasCouponCode() {
        return couponCode != null && !couponCode.isBlank();
    }

    public record OrderItemCommand(
            Long productId,
            int quantity,
            int size
    ) {}
}
