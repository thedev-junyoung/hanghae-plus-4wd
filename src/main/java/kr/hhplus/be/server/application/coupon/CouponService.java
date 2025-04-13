package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.coupon.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService implements CouponUseCase {

    private final CouponReader couponReader;
    private final CouponIssueWriter couponIssueWriter;
    private final CouponIssueReader couponIssueReader;

    // 주문 시 쿠폰 검증 및 할인 금액 반환
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

    // 주문 시 쿠폰 검증 및 할인 금액 반환
    @Override
    public ApplyCouponResult applyCoupon(ApplyCouponCommand command) {
        Coupon coupon = couponReader.findByCode(command.couponCode());
        coupon.validateUsable();

        if (!couponIssueReader.hasIssued(command.userId(), coupon.getId())) {
            throw new CouponException.NotIssuedException(command.userId(), command.couponCode());
        }

        Money discount = coupon.calculateDiscount(command.orderAmount());
        return ApplyCouponResult.from(coupon, discount);
    }
}
