package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.coupon.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService implements CouponUseCase {

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;

    @Override
    public CouponResult issueLimitedCoupon(IssueLimitedCouponCommand command) {
        Coupon coupon = couponRepository.findByCode(command.couponCode());

        // 중복 발급 방지
        if (couponIssueRepository.hasIssued(command.userId(), coupon.getId())) {
            throw new CouponException.AlreadyIssuedException(command.userId(), command.couponCode());
        }

        // 도메인 책임으로 발급 생성 및 수량 차감
        CouponIssue issue = CouponIssue.create(command.userId(), coupon);

        // 저장
        couponIssueRepository.save(issue);

        return CouponResult.from(issue);
    }

    @Override
    public ApplyCouponResult applyCoupon(ApplyCouponCommand command) {
        Coupon coupon = couponRepository.findByCode(command.couponCode());

        // 발급받은 쿠폰 이력 조회
        CouponIssue issue = couponIssueRepository.findByUserIdAndCouponId(command.userId(), coupon.getId())
                .orElseThrow(() -> new CouponException.NotIssuedException(command.userId(), command.couponCode()));

        // 쿠폰 유효성 및 사용 여부 체크
        issue.validateUsable();

        // 할인 금액 계산
        Money discount = issue.getCoupon().calculateDiscount(command.orderAmount());

        // 사용 처리
        issue.markAsUsed();

        return ApplyCouponResult.from(coupon, discount);
    }
}
