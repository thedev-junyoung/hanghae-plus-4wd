package kr.hhplus.be.server.application.productstatistics;

import kr.hhplus.be.server.application.product.PopularProductCriteria;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsId;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductStatisticsServiceIntegrationTest {

    @Autowired
    ProductStatisticsService service;

    @Autowired
    ProductStatisticsRepository repository;

    @Test
    @DisplayName("오늘자 통계가 없을 경우 새로 생성되어 저장된다")
    void record_createsNewStatistics() {
        // given
        Long productId = 10L; // 실제 존재하는 제품 ID (예: Reebok Classic Leather)
        LocalDate today = LocalDate.now();
        int quantity = 2;
        long unitAmount = 10000L;

        // 🧹 기존 통계가 있으면 삭제
        repository.findByProductIdAndStatDate(productId, today)
                .ifPresent(stat -> repository.delete(stat));

        // when
        service.record(new RecordSalesCommand(productId, quantity, unitAmount));

        // then
        ProductStatistics stats = repository.findByProductIdAndStatDate(productId, today)
                .orElseThrow(() -> new AssertionError("통계가 저장되지 않았습니다"));

        assertThat(stats.getSalesCount()).isEqualTo(2);
        assertThat(stats.getSalesAmount()).isEqualTo(20000L); // 2 * 10000
    }


    @Test
    @DisplayName("오늘자 통계가 존재하면 판매량과 금액이 누적된다")
    void record_accumulatesIfStatisticsExists() {
        // given
        Long productId = 9L; // Vans Old Skool
        LocalDate today = LocalDate.now();

        // clean up and setup
        repository.findByProductIdAndStatDate(productId, today).ifPresent(repository::delete);
        ProductStatistics existing = ProductStatistics.create(productId, today);
        existing.addSales(1, Money.wons(5000L));
        repository.save(existing);

        // when
        service.record(new RecordSalesCommand(productId, 2, 5000L));

        // then
        ProductStatistics stats = repository.findByProductIdAndStatDate(productId, today).orElseThrow();
        assertThat(stats.getSalesCount()).isEqualTo(3);
        assertThat(stats.getSalesAmount()).isEqualTo(15000L);
    }

    @Test
    @DisplayName("최근 3일간의 통계만 반영해 인기 상품을 판매량 기준으로 정렬한다")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findTopSellingProducts_shouldReturnSortedListWithinDateRange() {
        // given
        repository.deleteAll(); // 모든 통계 삭제 후 시작

        LocalDate today = LocalDate.now();
        LocalDate inRange = today.minusDays(2);
        LocalDate outOfRange = today.minusDays(10);

        Long productId1 = 1L; // New Balance 993
        Long productId2 = 2L; // ASICS GEL-Kayano 14

        ProductStatistics inRangeStat1 = new ProductStatistics(
                new ProductStatisticsId(productId1, inRange),
                5, Money.wons(50000)
        );
        ProductStatistics inRangeStat2 = new ProductStatistics(
                new ProductStatisticsId(productId2, today),
                10, Money.wons(120000)
        );
        ProductStatistics outOfRangeStat = new ProductStatistics(
                new ProductStatisticsId(productId1, outOfRange),
                100, Money.wons(1000000)
        );

        repository.saveAll(List.of(inRangeStat1, inRangeStat2, outOfRangeStat));

        PopularProductCriteria criteria = new PopularProductCriteria(3, 5);

        // when
        var results = service.getTopSellingProducts(criteria).stream().toList();

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).productId()).isEqualTo(productId2);
        assertThat(results.get(0).salesCount()).isEqualTo(10);
        assertThat(results.get(1).productId()).isEqualTo(productId1);
        assertThat(results.get(1).salesCount()).isEqualTo(5);
    }
}
