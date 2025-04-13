package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.product.DecreaseStockCommand;
import kr.hhplus.be.server.application.product.GetProductDetailCommand;

import java.util.List;

public record CreateOrderCommand(
        Long userId,
        List<OrderItemCommand> items
) {
    public record OrderItemCommand(
            Long productId,
            int quantity,
            int size
    ) {}

    public GetProductDetailCommand toGetProductDetailCommand() {
        return new GetProductDetailCommand(items.get(0).productId);
    }

    public DecreaseStockCommand toDecreaseStockCommand() {
        return new DecreaseStockCommand(items.get(0).productId, items.get(0).size, items.get(0).quantity);
    }
}
