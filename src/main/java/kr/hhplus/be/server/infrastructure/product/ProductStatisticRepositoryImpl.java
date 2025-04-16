package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.Optional;

@Repository
public class ProductStatisticRepositoryImpl implements ProductStatisticsRepository {
    @Override
    public Optional<ProductStatistics> findByProductIdAndStatDate(Long productId, LocalDate statDate) {
        return Optional.empty();
    }

    @Override
    public void save(ProductStatistics stats) {

    }
}
