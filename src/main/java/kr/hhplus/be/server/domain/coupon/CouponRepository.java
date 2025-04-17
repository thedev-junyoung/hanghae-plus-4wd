package kr.hhplus.be.server.domain.coupon;

public interface CouponRepository {
    void save(Coupon coupon);
    Coupon findByCode(String code); // 못 찾으면 CouponNotFoundException
}
