package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.application.productstatistics.ProductSalesInfo;
import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductStatisticRepositoryImpl implements ProductStatisticsRepository {

    private final ProductStatisticsJpaRepository jpaRepository;

    @Override
    public Optional<ProductStatistics> findByProductIdAndStatDate(Long productId, LocalDate statDate) {
        return jpaRepository.findByProductIdAndStatDate(productId, statDate);
    }

    @Override
    public void save(ProductStatistics stats) {
        jpaRepository.save(stats);
    }

    @Override
    public List<ProductSalesInfo> findTopSellingProducts(LocalDate from, LocalDate to, int limit) {
        return jpaRepository.findTopSellingProducts(from, to).stream()
                .limit(limit)
                .toList();
    }
}
