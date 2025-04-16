package kr.hhplus.be.server.infrastructure.coupon;


import kr.hhplus.be.server.domain.coupon.CouponIssue;
import kr.hhplus.be.server.domain.coupon.CouponIssueRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouponIssueRepositoryImpl implements CouponIssueRepository {
    @Override
    public CouponIssue save(CouponIssue issue) {
        return null;
    }

    @Override
    public boolean hasIssued(Long userId, Long couponId) {
        return false;
    }

    @Override
    public Optional<CouponIssue> findByUserIdAndCouponId(Long userId, Long couponId) {
        return Optional.empty();
    }
}
