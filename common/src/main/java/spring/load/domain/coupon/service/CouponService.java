package spring.load.domain.coupon.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.entity.CouponStatus;
import spring.load.domain.coupon.repository.CouponRepository;
import spring.load.domain.member.entity.Member;
import spring.load.domain.member.repository.MemberRepository;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    
    public CouponService(CouponRepository couponRepository, MemberRepository memberRepository) {
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
    }

    public Coupon issue(Long memberId){
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));
        
        List<Coupon> existingCoupons = couponRepository.findByMemberId(memberId);
        if(!existingCoupons.isEmpty()){
            throw new RuntimeException("이미 쿠폰을 발급받았습니다.");
        }

        Coupon coupon = new Coupon();
        coupon.setMember(member);
        coupon.setCouponCode(generateCouponCode());
        coupon.setDiscountAmount(1000L);
        coupon.setStatus(CouponStatus.ISSUED);
        coupon.setIssuedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    private String generateCouponCode(){
        return "coupon-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

