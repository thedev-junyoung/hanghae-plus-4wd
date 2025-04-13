package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_issue")
@Getter
@NoArgsConstructor
public class CouponIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    // 생성자
    public CouponIssue(Long userId, Coupon coupon) {
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = LocalDateTime.now();
    }

    // 정적 팩토리 메서드 (선호하는 스타일이면 유지)
    public static CouponIssue create(Long userId, Coupon coupon) {
        return new CouponIssue(userId, coupon);
    }
}
