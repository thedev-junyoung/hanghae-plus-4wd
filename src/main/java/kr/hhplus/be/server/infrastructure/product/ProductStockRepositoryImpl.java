package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductStockRepositoryImpl implements ProductStockRepository {
    @Override
    public Optional<ProductStock> findByProductIdAndSize(Long productId, int size) {
        return Optional.empty();
    }

    @Override
    public ProductStock save(ProductStock stock) {
        return null;
    }

    @Override
    public Optional<ProductStock> findByProductId(Long id) {
        return Optional.empty();
    }
}
