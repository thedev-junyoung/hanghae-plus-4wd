package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    private String id;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @Column(nullable = false)
    private long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private OrderStatus status;
    @Column(nullable = false, updatable = false)

    private LocalDateTime createdAt;

    public static Order create(String id, Long userId, List<OrderItem> items, Money totalAmount) {
        if (items == null || items.isEmpty()) {
            throw new OrderException.EmptyItemException();
        }

        long expectedTotal = items.stream()
                .mapToLong(item -> item.getPrice().multiply(item.getQuantity()).value())
                .sum();

        if (expectedTotal != totalAmount.value()) {
            throw new OrderException.InvalidTotalAmountException(expectedTotal, totalAmount.value());
        }

        Order order = new Order();
        order.id = id;
        order.userId = userId;
        order.items = items;
        order.totalAmount = totalAmount.value();
        order.status = OrderStatus.CREATED;
        order.createdAt = LocalDateTime.now();

        for (OrderItem item : items) {
            item.initOrder(order);
        }

        return order;
    }

    public void cancel() {
        if (!status.canCancel()) {
            throw new OrderException.InvalidStateException(status, "cancel()");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void markConfirmed() {
        if (!status.canConfirm()) {
            throw new OrderException.InvalidStateException(status, "markConfirmed()");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void validatePayable() {
        if (status != OrderStatus.CREATED) {
            throw new OrderException.InvalidStateException(status, "payment");
        }
    }
}

