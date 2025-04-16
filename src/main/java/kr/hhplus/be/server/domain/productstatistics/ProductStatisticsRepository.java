package kr.hhplus.be.server.domain.productstatistics;


import kr.hhplus.be.server.application.productstatistics.ProductSalesInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductStatisticsRepository {
    Optional<ProductStatistics> findByProductIdAndStatDate(Long productId, LocalDate statDate);
    void save(ProductStatistics stats);

    List<ProductSalesInfo> findTopSellingProducts(LocalDate from, LocalDate to, int limit);

}
