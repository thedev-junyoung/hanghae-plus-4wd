package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {

    private final ProductStockJpaRepository jpaRepository;

    @Override
    public Optional<ProductStock> findByProductIdAndSize(Long productId, int size) {
        return jpaRepository.findByProductIdAndSize(productId, size);
    }

    @Override
    public List<ProductStock> findAllByProductId(Long productId) {
        return List.of();
    }

    @Override
    public ProductStock save(ProductStock stock) {
        return jpaRepository.save(stock);
    }

    @Override
    public Optional<ProductStock> findByProductId(Long id) {
        return jpaRepository.findByProductId(id);
    }
}
