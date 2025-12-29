package spring.load.domain.coupon.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.entity.CouponEvent;
import spring.load.domain.coupon.entity.CouponStatus;
import spring.load.domain.coupon.repository.CouponEventRepository;
import spring.load.domain.coupon.repository.CouponRepository;
import spring.load.domain.member.entity.Member;
import spring.load.domain.member.repository.MemberRepository;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponEventRepository couponEventRepository;
    private final MemberRepository memberRepository;
    
    public CouponService(
            CouponRepository couponRepository,
            CouponEventRepository couponEventRepository,
            MemberRepository memberRepository) {
        this.couponRepository = couponRepository;
        this.couponEventRepository = couponEventRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Coupon issue(Long memberId, Long eventId) {
        // 회원 확인
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));
        
        // 이벤트 확인 및 재고 체크 (Pessimistic Lock으로 동시성 제어)
        CouponEvent event = couponEventRepository.findByIdWithLock(eventId)
            .orElseThrow(() -> new RuntimeException("쿠폰 이벤트를 찾을 수 없습니다"));
        
        // 이벤트 유효성 체크
        if (!event.isAvailable()) {
            throw new RuntimeException("쿠폰 이벤트가 진행 중이 아니거나 재고가 없습니다");
        }
        
        // 사용자별 중복 발급 체크
        Long existingCouponCount = couponRepository.countByMemberIdAndEventId(memberId, eventId);
        if (existingCouponCount > 0) {
            throw new RuntimeException("이미 쿠폰을 발급받았습니다");
        }
        
        // 재고 감소
        if (!event.decreaseStock()) {
            throw new RuntimeException("쿠폰 재고가 부족합니다");
        }
        
        // 쿠폰 발급
        Coupon coupon = new Coupon();
        coupon.setMember(member);
        coupon.setEvent(event);
        coupon.setCouponCode(generateCouponCode());
        coupon.setStatus(CouponStatus.ISSUED);
        coupon.setIssuedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    private String generateCouponCode(){
        return "coupon-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
