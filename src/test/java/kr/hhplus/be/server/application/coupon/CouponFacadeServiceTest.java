package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

// CouponFacadeServiceTest.java

@ExtendWith(MockitoExtension.class)
class CouponFacadeServiceTest {

    @Mock
    private CouponReader couponReader;

    @Mock
    private CouponIssueWriter couponIssueWriter;

    @InjectMocks
    private CouponFacadeService couponFacadeService;

    @Test
    @DisplayName("쿠폰 정상 발급 성공")
    void issueCoupon_success() {
        // given
        String couponCode = "WELCOME10";
        long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Coupon coupon = Coupon.create(
                couponCode,
                CouponType.PERCENTAGE,
                10,
                100,
                now.minusDays(1),
                now.plusDays(1)
        );

        CouponIssue issued = new CouponIssue(userId, coupon);
        IssueLimitedCouponCommand command = new IssueLimitedCouponCommand(userId, couponCode);

        given(couponReader.findByCode(couponCode)).willReturn(coupon);
        given(couponIssueWriter.hasIssued(userId, coupon.getId())).willReturn(false);
        given(couponIssueWriter.save(userId, coupon)).willReturn(issued);

        // when
        CouponResult result = couponFacadeService.issueLimitedCoupon(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        verify(couponReader).findByCode(couponCode);
        verify(couponIssueWriter).save(userId, coupon);
    }

    @Test
    @DisplayName("이미 쿠폰을 발급받은 경우 예외 발생")
    void issueCoupon_fail_ifAlreadyIssued() {
        // given
        String couponCode = "DUPLICATE";
        long userId = 1L;
        Coupon coupon = Coupon.create(
                couponCode,
                CouponType.FIXED,
                3000,
                10,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        IssueLimitedCouponCommand command = new IssueLimitedCouponCommand(userId, couponCode);

        given(couponReader.findByCode(couponCode)).willReturn(coupon);
        given(couponIssueWriter.hasIssued(userId, coupon.getId())).willReturn(true);

        // when & then
        assertThrows(CouponException.AlreadyIssuedException.class, () ->
                couponFacadeService.issueLimitedCoupon(command));
    }

    @Test
    @DisplayName("만료된 쿠폰일 경우 예외 발생")
    void issueCoupon_fail_ifExpired() {
        // given
        Coupon coupon = Coupon.create(
                "EXPIRED",
                CouponType.FIXED,
                3000,
                10,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1)
        );
        IssueLimitedCouponCommand command = new IssueLimitedCouponCommand(1L, "EXPIRED");

        given(couponReader.findByCode("EXPIRED")).willReturn(coupon);

        // when & then
        assertThrows(CouponException.ExpiredException.class, () ->
                couponFacadeService.issueLimitedCoupon(command));
    }

    @Test
    @DisplayName("수량 소진된 쿠폰일 경우 예외 발생")
    void issueCoupon_fail_ifSoldOut() {
        // given
        Coupon coupon = new Coupon(
                "SOLDOUT",
                CouponType.PERCENTAGE,
                20,
                10,
                0,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        IssueLimitedCouponCommand command = new IssueLimitedCouponCommand(1L, "SOLDOUT");

        given(couponReader.findByCode("SOLDOUT")).willReturn(coupon);

        // when & then
        assertThrows(CouponException.AlreadyExhaustedException.class, () ->
                couponFacadeService.issueLimitedCoupon(command));
    }
}

