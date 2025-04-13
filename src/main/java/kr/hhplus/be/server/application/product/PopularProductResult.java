package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;


public record PopularProductResult(
        Long id,
        String name,
        long price,
        int salesCount
) {
    public static PopularProductResult from(Product product, int salesCount) {
        return new PopularProductResult(
                product.getId(),
                product.getName(),
                product.getPrice(),
                salesCount
        );
    }
}

