package kr.hhplus.be.server.domain.coupon;

public interface CouponIssueReader {
    boolean hasIssued(Long aLong, Long id);
}
