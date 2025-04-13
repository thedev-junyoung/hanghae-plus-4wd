package kr.hhplus.be.server.domain.coupon;

public interface CouponIssueWriter {
    boolean hasIssued(Long userId, Long couponId);
    CouponIssue save(Long userId, Coupon coupon);
}