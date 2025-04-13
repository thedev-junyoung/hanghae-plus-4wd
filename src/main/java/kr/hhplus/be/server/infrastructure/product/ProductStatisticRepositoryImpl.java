package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public List<ProductStatistics> findTopSellingProductsAfter(LocalDateTime from, int limit) {
        return List.of();
    }
}
