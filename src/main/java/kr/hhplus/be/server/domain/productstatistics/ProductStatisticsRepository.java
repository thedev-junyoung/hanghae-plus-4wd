package kr.hhplus.be.server.domain.productstatistics;


import java.time.LocalDate;
import java.util.Optional;

public interface ProductStatisticsRepository {
    Optional<ProductStatistics> findByProductIdAndStatDate(Long productId, LocalDate statDate);
    void save(ProductStatistics stats);
}
