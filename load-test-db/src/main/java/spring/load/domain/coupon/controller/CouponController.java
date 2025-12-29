package spring.load.domain.coupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.load.domain.coupon.dto.CouponIssueRequest;
import spring.load.domain.coupon.dto.CouponIssueResponse;
import spring.load.domain.coupon.dto.ErrorResponse;
import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.service.CouponService;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/issue")
    public ResponseEntity<?> issueCoupon(
        @RequestBody CouponIssueRequest request
    ){
        try {
            Coupon coupon = couponService.issue(request.getMemberId(), request.getEventId());
            CouponIssueResponse response = CouponIssueResponse.from(coupon);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), "COUPON_ISSUE_ERROR");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
