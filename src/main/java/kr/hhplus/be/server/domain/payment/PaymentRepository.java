package kr.hhplus.be.server.domain.payment;


import java.util.Optional;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findById(String id);

    Optional<Payment> findByOrderId(String id);
}
