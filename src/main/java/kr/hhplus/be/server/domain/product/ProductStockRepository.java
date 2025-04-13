package kr.hhplus.be.server.domain.product;

import java.util.Optional;

public interface ProductStockRepository {
    Optional<ProductStock> findByProductIdAndSize(Long productId, int size);
    ProductStock save(ProductStock stock);

    Optional<ProductStock> findByProductId(Long id);
}
