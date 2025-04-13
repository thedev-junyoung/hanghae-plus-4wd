package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public void save(Order order) {

    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.empty();
    }
}
