package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void save(Product domain) {

    }

    @Override
    public Optional<Product> findById(Long aLong) {
        return Optional.empty();
    }
}
