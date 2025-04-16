package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.application.productstatistics.ProductSalesInfo;
import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductStatisticsJpaRepository extends JpaRepository<ProductStatistics, Long> {
    Optional<ProductStatistics> findByProductIdAndStatDate(Long productId, LocalDate statDate);

    @Query("""
        SELECT new kr.hhplus.be.server.application.productstatistics.ProductSalesInfo(
            ps.id.productId,
            SUM(ps.salesCount)
        )
        FROM ProductStatistics ps
        WHERE ps.id.statDate BETWEEN :from AND :to
        GROUP BY ps.id.productId
        ORDER BY SUM(ps.salesCount) DESC
        """)
    List<ProductSalesInfo> findTopSellingProducts(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
