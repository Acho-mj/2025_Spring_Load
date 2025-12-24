package spring.load.domain.coupon.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.entity.CouponStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueResponse {
    private Long id;
    private Long memberId;
    private String couponCode;
    private Long discountAmount;
    private CouponStatus status;
    private LocalDateTime issuedAt;

    public static CouponIssueResponse from(Coupon coupon) {
        return new CouponIssueResponse(
                coupon.getId(),
                coupon.getMember().getId(),
                coupon.getCouponCode(),
                coupon.getDiscountAmount(),
                coupon.getStatus(),
                coupon.getIssuedAt()
        );
    }
}
