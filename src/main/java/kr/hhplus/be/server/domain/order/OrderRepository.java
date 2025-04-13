package kr.hhplus.be.server.domain.order;


import java.util.Optional;

public interface OrderRepository{
    void save(Order order);
    Optional<Order> findById(String orderId);
}
