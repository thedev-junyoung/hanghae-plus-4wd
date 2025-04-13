package kr.hhplus.be.server.application.productstatistics;

import kr.hhplus.be.server.application.product.PopularProductCriteria;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.productstatistics.ProductStatistics;
import kr.hhplus.be.server.domain.productstatistics.ProductStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStatisticsService implements ProductStatisticsUseCase {

    private final ProductStatisticsRepository repository;

    @Override
    public void record(RecordSalesCommand command) {
        LocalDate today = LocalDate.now();
        ProductStatistics stats = repository.findByProductIdAndStatDate(command.productId(), today)
                .orElseGet(() -> ProductStatistics.create(command.productId(), today));
        stats.addSales(command.quantity(), Money.wons(command.amount()));
        repository.save(stats);
    }

    @Override
    public Collection<ProductSalesInfo> getTopSellingProducts(PopularProductCriteria criteria) {
        return List.of();
    }

}
