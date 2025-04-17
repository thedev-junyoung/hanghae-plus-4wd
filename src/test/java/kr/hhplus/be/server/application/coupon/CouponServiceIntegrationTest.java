package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.coupon.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
class CouponServiceIntegrationTest {

    @Autowired
    CouponUseCase couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    Long userId = 1L;

    @BeforeEach
    void setUp() {
        Coupon coupon = Coupon.create(
                "LIMITED50",
                CouponType.FIXED,
                5_000,
                10,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(7)
        );
        couponRepository.save(coupon);
    }

    @Test
    @DisplayName("쿠폰 발급 시 coupon_issue 테이블에 저장된다")
    void issueLimitedCoupon_savedToDatabase() {
        // given
        IssueLimitedCouponCommand command = IssueLimitedCouponCommand.of(userId, "LIMITED50");

        // when
        CouponResult result = couponService.issueLimitedCoupon(command);

        // then
        CouponIssue issue = couponIssueRepository.findByUserIdAndCouponId(userId, result.userCouponId())
                .orElseThrow(() -> new AssertionError("coupon_issue not saved"));

        assertThat(issue.getUserId()).isEqualTo(userId);
        assertThat(issue.getCoupon().getCode()).isEqualTo("LIMITED50");
        assertThat(issue.isUsed()).isFalse();
    }


    @Test
    @DisplayName("쿠폰을 적용하면 isUsed 플래그가 true로 변경된다")
    void applyCoupon_marksAsUsed() {
        // given
        couponService.issueLimitedCoupon(IssueLimitedCouponCommand.of(userId, "LIMITED50"));
        ApplyCouponCommand command = ApplyCouponCommand.of(userId, "LIMITED50", Money.from(20_000L));

        // when
        couponService.applyCoupon(command);

        // then
        Coupon coupon = couponRepository.findByCode("LIMITED50");
        CouponIssue issue = couponIssueRepository.findByUserIdAndCouponId(userId, coupon.getId())
                .orElseThrow(() -> new AssertionError("coupon_issue not found"));

        assertThat(issue.isUsed()).isTrue();
    }

    @Test
    @DisplayName("쿠폰 발급 시 남은 수량이 1 감소한다")
    void issueLimitedCoupon_decreaseQuantity() {
        // given
        Coupon before = couponRepository.findByCode("LIMITED50");
        int prevRemaining = before.getRemainingQuantity();

        // when
        couponService.issueLimitedCoupon(IssueLimitedCouponCommand.of(userId, "LIMITED50"));

        // then
        Coupon after = couponRepository.findByCode("LIMITED50");
        assertThat(after.getRemainingQuantity()).isEqualTo(prevRemaining - 1);
    }


    @Test
    @DisplayName("같은 쿠폰을 두 번 발급받으면 예외가 발생한다")
    void issueLimitedCoupon_twice_fail() {
        couponService.issueLimitedCoupon(IssueLimitedCouponCommand.of(userId, "LIMITED50"));

        assertThatThrownBy(() ->
                couponService.issueLimitedCoupon(IssueLimitedCouponCommand.of(userId, "LIMITED50"))
        ).isInstanceOf(CouponException.AlreadyIssuedException.class);
    }
}

