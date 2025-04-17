package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsId;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductFacadeIntegrationTest {

    @Autowired
    ProductFacade productFacade;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductStatisticsRepository productStatisticsRepository;

    @Test
    @DisplayName("인기 상품을 조회하면 판매량 기준으로 정렬된 상품 정보를 반환한다")
    void getPopularProducts_success() {
        // given
        Product p1 = productRepository.save(Product.create(
                "New Balance 993", "New Balance", Money.wons(199000),
                LocalDate.of(2024, 4, 1), "http://example.com/nb993.jpg", "미국산 프리미엄 쿠셔닝"));

        Product p2 = productRepository.save(Product.create(
                "Air Force 1", "Nike", Money.wons(139000),
                LocalDate.of(2024, 3, 10), "http://example.com/af1.jpg", "클래식 로우탑"));

        // 날짜 기준
        LocalDate today = LocalDate.now();

        // 통계 데이터 생성 + 판매량 누적
        ProductStatistics stat1 = ProductStatistics.create(p1.getId(), today);
        stat1.addSales(15, Money.wons(p1.getPrice())); // 총 15건

        ProductStatistics stat2 = ProductStatistics.create(p2.getId(), today);
        stat2.addSales(10, Money.wons(p2.getPrice())); // 총 10건

        productStatisticsRepository.saveAll(List.of(stat1, stat2));

        // when
        PopularProductCriteria criteria = new PopularProductCriteria(7, 2);
        List<PopularProductResult> results = productFacade.getPopularProducts(criteria);

        ProductStatistics loaded = productStatisticsRepository
                .findById(new ProductStatisticsId(p1.getId(), today))
                .orElseThrow();

        assertThat(loaded.getSalesCount()).isEqualTo(15);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).name()).isEqualTo("New Balance 993");
        assertThat(results.get(0).salesCount()).isEqualTo(15);
        assertThat(results.get(1).name()).isEqualTo("Air Force 1");
        assertThat(results.get(1).salesCount()).isEqualTo(10);
    }

}
