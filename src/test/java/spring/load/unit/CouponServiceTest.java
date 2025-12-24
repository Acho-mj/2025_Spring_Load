package spring.load.unit;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.entity.CouponStatus;
import spring.load.domain.coupon.repository.CouponRepository;
import spring.load.domain.coupon.service.CouponService;
import spring.load.domain.member.entity.Member;
import spring.load.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Coupon Service 테스트")
public class CouponServiceTest {
    @Mock
    private CouponRepository couponRepository;
    
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CouponService couponService; 

    private Member createMember() {
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setName("테스트");
        return member;
    }
    
    @Test
    @DisplayName("회원이 쿠폰을 발급받으면 쿠폰이 저장되어야 한다")
    void 회원이_쿠폰을_발급받으면_쿠폰이_저장되어야_한다() {
        // Given: 회원이 존재하고
        Member member = createMember();
        when(memberRepository.findById(1L))
            .thenReturn(Optional.of(member));
        when(couponRepository.findByMemberId(1L))
            .thenReturn(List.of());
        when(couponRepository.save(any(Coupon.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When: 쿠폰 발급을 요청하면
        Coupon result = couponService.issue(1L);

        // Then: 쿠폰이 발급되어 저장되어야 한다
        assertThat(result).isNotNull();
        assertThat(result.getMember().getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(CouponStatus.ISSUED);
        assertThat(result.getCouponCode()).isNotNull();
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("존재하지 않는 회원에게는 쿠폰을 발급할 수 없다")
    void 존재하지_않는_회원에게는_쿠폰을_발급할_수_없다() {
        // Given: 존재하지 않는 회원 ID가 있고
        when(memberRepository.findById(999L))
            .thenReturn(Optional.empty());

        // When: 쿠폰 발급을 요청하면
        assertThatThrownBy(() -> couponService.issue(999L))
            .isInstanceOf(RuntimeException.class);
        verify(couponRepository, never()).save(any(Coupon.class));
    }
    
    @Test
    @DisplayName("이미 쿠폰을 발급받은 회원은 중복 발급이 불가능하다")
    void 이미_쿠폰을_발급받은_회원은_중복_발급이_불가능하다() {
        // Given: 쿠폰을 이미 발급받은 회원이 있고
        Member member = createMember();
        Coupon existingCoupon = new Coupon();
        existingCoupon.setMember(member);
        existingCoupon.setStatus(CouponStatus.ISSUED);
        
        when(memberRepository.findById(1L))
            .thenReturn(Optional.of(member));
        when(couponRepository.findByMemberId(1L))
            .thenReturn(List.of(existingCoupon));

        // When: 다시 쿠폰 발급을 요청하면
        // Then: 예외가 발생해야 한다
        assertThatThrownBy(() -> couponService.issue(1L))
            .isInstanceOf(RuntimeException.class);
        verify(couponRepository, never()).save(any(Coupon.class));
    }
}
