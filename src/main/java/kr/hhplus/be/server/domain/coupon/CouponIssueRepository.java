package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface CouponIssueRepository {

    CouponIssue save(CouponIssue issue); // 바꿔주세요
    boolean hasIssued(Long userId, Long couponId);
    Optional<CouponIssue> findByUserIdAndCouponId(Long userId, Long couponId);
}
