package kr.hhplus.be.server.domain.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository{
    Collection<Product> findTopSellingProducts();
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);

    void save(Product domain);
    Optional<Product> findById(Long aLong);

}
