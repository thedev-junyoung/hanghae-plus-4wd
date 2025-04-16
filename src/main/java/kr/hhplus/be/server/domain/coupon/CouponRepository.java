package kr.hhplus.be.server.domain.coupon;

public interface CouponRepository {
    Coupon findByCode(String code); // 못 찾으면 CouponNotFoundException
}
