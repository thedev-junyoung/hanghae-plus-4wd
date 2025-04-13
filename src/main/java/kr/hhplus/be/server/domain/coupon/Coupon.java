package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Column(nullable = false)
    private Integer discountRate;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer remainingQuantity;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    public Coupon(String code, CouponType type, Integer discountRate, Integer totalQuantity,
                  Integer remainingQuantity, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.code = code;
        this.type = type;
        this.discountRate = discountRate;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = remainingQuantity;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public static Coupon create(String code, CouponType type, Integer discountRate, Integer totalQuantity,
                                 LocalDateTime validFrom, LocalDateTime validUntil) {
        return new Coupon(code, type, discountRate, totalQuantity, totalQuantity, validFrom, validUntil);
    }


    public boolean isExpired() {
        return Policy.isExpired(validUntil);
    }

    public boolean isExhausted() {
        return Policy.isExhausted(remainingQuantity);
    }

    public void validateUsable() {
        if (isExpired()) {
            throw new CouponException.ExpiredException();
        }
        if (isExhausted()) {
            throw new CouponException.AlreadyExhaustedException();
        }
    }

    public void decreaseQuantity() {
        validateUsable();
        this.remainingQuantity -= 1;
    }

    static class Policy {
        public static boolean isExpired(LocalDateTime until) {
            return LocalDateTime.now().isAfter(until);
        }

        public static boolean isExhausted(int quantity) {
            return quantity <= 0;
        }
    }
}
