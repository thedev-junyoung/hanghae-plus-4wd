package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.order.CreateOrderCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private List<OrderItemRequest> items;

    public CreateOrderCommand toCommand() {
        List<CreateOrderCommand.OrderItemCommand> itemCommands = items.stream()
                .map(i -> new CreateOrderCommand.OrderItemCommand(i.productId, i.quantity, i.size))
                .toList();
        return new CreateOrderCommand(userId, itemCommands);
    }

    @Getter
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        private Long productId;

        @NotNull
        private int quantity;

        @NotNull
        private int size;
    }
}
