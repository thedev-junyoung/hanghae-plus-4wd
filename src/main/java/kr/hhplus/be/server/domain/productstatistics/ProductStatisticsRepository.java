package kr.hhplus.be.server.domain.productstatistics;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductStatisticsRepository {
    Optional<ProductStatistics> findByProductIdAndStatDate(Long productId, LocalDate statDate);
    void save(ProductStatistics stats);
    List<ProductStatistics> findTopSellingProductsAfter(LocalDateTime from, int limit);
}
