package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponFacadeService implements CouponUseCase {

    private final CouponReader couponReader;
    private final CouponIssueWriter couponIssueWriter;

    @Override
    public CouponResult issueLimitedCoupon(IssueLimitedCouponCommand command) {
        Coupon coupon = couponReader.findByCode(command.couponCode());

        coupon.validateUsable();

        if (couponIssueWriter.hasIssued(command.userId(), coupon.getId())) {
            throw new CouponException.AlreadyIssuedException(command.userId(), command.couponCode());
        }

        CouponIssue issue = couponIssueWriter.save(command.userId(), coupon);

        return CouponResult.from(issue);
    }

}
