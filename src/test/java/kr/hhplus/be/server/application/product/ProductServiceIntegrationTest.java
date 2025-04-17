package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import kr.hhplus.be.server.domain.product.*;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품 상세 조회 시 재고 정보도 함께 반환된다")
    void getProductDetail_shouldIncludeStockQuantity() {
        // given
        Long productId = 1L;
        int size = 270;

        // when
        ProductDetailResult result = productService.getProductDetail(
                new GetProductDetailCommand(productId, size)
        );

        // then
        assertThat(result.product().stockQuantity()).isEqualTo(50); // 태스트 컨테이너에서 입력한 재고 값
    }


    @Test
    @DisplayName("출시 전 상품은 주문할 수 없다")
    void validateOrderable_shouldFailIfNotReleased() {
        // given
        Product product = Product.create("조던 미래", "Nike", Money.wons(150000),
                LocalDate.now().plusDays(1), "fut.jpg", "미래 신발");
        productRepository.save(product);

        // when & then
        assertThatThrownBy(() -> product.validateOrderable(1))
                .isInstanceOf(ProductException.NotReleasedException.class);
    }

    @Test
    @DisplayName("재고가 0이면 주문할 수 없다")
    void validateOrderable_shouldFailIfOutOfStock() {
        // given
        Product product = Product.create("조던 제로", "Nike", Money.wons(150000),
                LocalDate.now().minusDays(1), "zero.jpg", "없는 재고");
        productRepository.save(product);

        // when & then
        assertThatThrownBy(() -> product.validateOrderable(0))
                .isInstanceOf(ProductException.OutOfStockException.class);
    }
}

