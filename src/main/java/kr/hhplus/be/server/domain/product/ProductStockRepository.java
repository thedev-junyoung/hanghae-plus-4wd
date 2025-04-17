package kr.hhplus.be.server.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductStockRepository {
    ProductStock save(ProductStock stock);

    Optional<ProductStock> findByProductId(Long id);
    Optional<ProductStock> findByProductIdAndSize(Long productId, int size);
    List<ProductStock> findAllByProductId(Long productId); // 재고 전체 필요할 경우
}
