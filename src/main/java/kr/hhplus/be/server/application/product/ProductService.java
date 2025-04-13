package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    @Override
    public ProductListResult getProductList(GetProductListCommand command) {
        PageRequest pageRequest = PageRequest.of(command.page(), command.size(), command.getSort());

        var productPage = productRepository.findAll(pageRequest);

        List<ProductInfo> infos = productPage.getContent().stream()
                .map(product -> {
                    int stock = productStockRepository.findByProductId(product.getId())
                            .map(ProductStock::getStockQuantity)
                            .orElse(0);
                    return ProductInfo.from(product, stock);
                })
                .toList();

        return ProductListResult.from(infos);
    }

    @Override
    public ProductDetailResult getProductDetail(GetProductDetailCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductException.NotFoundException(command.productId()));

        int stock = productStockRepository.findByProductId(product.getId())
                .map(ProductStock::getStockQuantity)
                .orElse(0);

        return ProductDetailResult.fromDomain(product, stock);
    }

    @Override
    public boolean decreaseStock(DecreaseStockCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductException.NotFoundException(command.productId()));

        ProductStock stock = productStockRepository.findByProductIdAndSize(command.productId(), command.size())
                .orElseThrow(ProductException.InsufficientStockException::new);

        stock.decreaseStock(command.quantity());
        productStockRepository.save(stock);
        return true;
    }

    @Override
    public Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException.NotFoundException(productId));
    }
}

