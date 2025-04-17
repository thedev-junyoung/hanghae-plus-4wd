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
        Long productId = 100L;
        int quantity = 2;
        long amount = 10000L;
        LocalDate today = LocalDate.now();

        // when
        service.record(new RecordSalesCommand(productId, quantity, amount));

        // then: DB에 실제 저장된 값 확인
        ProductStatistics stats = repository.findByProductIdAndStatDate(productId, today).orElseThrow();
        assertThat(stats.getSalesCount()).isEqualTo(2);
        assertThat(stats.getSalesAmount()).isEqualTo(20000L); // 2 * 10000
    }

    @Test
    @DisplayName("오늘자 통계가 존재하면 판매량과 금액이 누적된다")
    void record_accumulatesIfStatisticsExists() {
        // given
        Long productId = 200L;
        LocalDate today = LocalDate.now();

        ProductStatistics existing = ProductStatistics.create(productId, today);
        existing.addSales(1, Money.wons(5000L));
        repository.save(existing);

        // when
        service.record(new RecordSalesCommand(productId, 2, 5000L));

        // then: 누적된 값 검증
        ProductStatistics stats = repository.findByProductIdAndStatDate(productId, today).orElseThrow();
        assertThat(stats.getSalesCount()).isEqualTo(3);
        assertThat(stats.getSalesAmount()).isEqualTo(15000L);
    }

    @Test
    @DisplayName("최근 3일간의 통계만 반영해 인기 상품을 판매량 기준으로 정렬한다")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findTopSellingProducts_shouldReturnSortedListWithinDateRange() {
        // given
        // 테스트 전에 기존 데이터 삭제
        repository.deleteAll();

        // given
        LocalDate today = LocalDate.now();
        LocalDate inRange = today.minusDays(3);
        LocalDate outOfRange = today.minusDays(10);

        Long productId1 = 1L;
        Long productId2 = 2L;

        ProductStatistics inRangeStat1 = new ProductStatistics(
                new ProductStatisticsId(productId1, inRange),
                5, // 판매수량
                Money.wons(50000) // 총 판매금액 (5 * 10000)
        );

        ProductStatistics inRangeStat2 = new ProductStatistics(
                new ProductStatisticsId(productId2, today),
                10, // 판매수량
                Money.wons(120000) // 총 판매금액 (10 * 12000)
        );

        ProductStatistics outOfRangeStat = new ProductStatistics(
                new ProductStatisticsId(productId1, outOfRange),
                100,
                Money.wons(1000000)
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

        // DB 기준 검증도 명확하게
        ProductStatistics latestStat = repository.findByProductIdAndStatDate(productId2, today).orElseThrow();
        assertThat(latestStat.getSalesCount()).isEqualTo(10);
        assertThat(latestStat.getSalesAmount()).isEqualTo(120000L);

        ProductStatistics excludedStat = repository.findByProductIdAndStatDate(productId1, outOfRange).orElseThrow();
        assertThat(excludedStat.getSalesCount()).isEqualTo(100);
    }

}
