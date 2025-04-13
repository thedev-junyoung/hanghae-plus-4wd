package kr.hhplus.be.server.application.coupon;

public interface CouponUseCase {
    /**
     * 쿠폰을 생성합니다
     */
    CouponResult issueLimitedCoupon(IssueLimitedCouponCommand command);
}
