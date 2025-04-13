package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.common.vo.Money;

public record ApplyCouponCommand(
        Long userId,
        String couponCode,
        Money orderAmount
) {}
