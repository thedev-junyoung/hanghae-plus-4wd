package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.CouponIssueReader;
import org.springframework.stereotype.Repository;

@Repository
public class CouponIssueReaderImpl implements CouponIssueReader {
    @Override
    public boolean hasIssued(Long aLong, Long id) {
        return false;
    }
}
