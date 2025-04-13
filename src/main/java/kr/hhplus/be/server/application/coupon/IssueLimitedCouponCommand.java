package kr.hhplus.be.server.application.coupon;

public record IssueLimitedCouponCommand(
        Long userId,
        String couponCode
) {

}
