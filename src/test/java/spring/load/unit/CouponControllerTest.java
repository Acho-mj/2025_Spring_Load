package spring.load.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.entity.CouponStatus;
import spring.load.domain.coupon.service.CouponService;
import spring.load.domain.member.entity.Member;
import spring.load.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;

@WebMvcTest
@DisplayName("Coupon Controller 테스트")
public class CouponControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @MockBean
    private MemberRepository memberRepository;

    private Member createMember(){
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@test.com");
        member.setName("test");
        return member;
    }
    
    private Coupon createCoupon(Member member) {
        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setMember(member);
        coupon.setCouponCode("COUPON-12345678");
        coupon.setDiscountAmount(1000L);
        coupon.setStatus(CouponStatus.ISSUED);
        coupon.setIssuedAt(LocalDateTime.now());
        return coupon;
    }

    @Test
    @DisplayName("회원이 쿠폰 발급을 요청하면 쿠폰이 발급되어야 한다")
    void 회원이_쿠폰_발급을_요청하면_쿠폰이_발급되어야_한다() throws Exception {
        // Given: 회원이 존재하고
        Member member = createMember();
        Coupon coupon = createCoupon(member);
        CouponIssueRequest request = new CouponIssueRequest(1L);

        when(couponService.issue(1L)).thenReturn(coupon);

        // When & Then: 쿠폰 발급을 요청하면
        mockMvc.perform(post("api/coupons/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.memberId").value(1L))
                    .andExpect(jsonPath("$.couponCode").value("coupon-001"))
                    .andExpect(jsonPath("$.discountAmount").value(1000L))
                    .andExpect(jsonPath("$.status").value("ISSUED"))
                    .andExpect(jsonPath("$.issuedAt").exists());
    }

    @Test
    @DisplayName("존재하지 않는 회원에게 쿠폰 발급을 요청하면 400 에러가 발생해야 한다")
    void 존재하지_않는_회원에게_쿠폰_발급을_요청하면_400_에러가_발생해야_한다() throws Exception {
        // Given: 존재하지 않는 회원 ID가 있고
        CouponIssueRequest request = new CouponIssueRequest(999L);
        when(couponService.issue(999L))
            .thenThrow(new RuntimeException("회원을 찾을 수 없습니다"));

        // When & Then: 쿠폰 발급을 요청하면
        mockMvc.perform(post("api/coupons/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이미 쿠폰을 발급받은 회원이 다시 요청하면 400 에러가 발생해야 한다")
    void 이미_쿠폰을_발급받은_회원이_다시_요청하면_400_에러가_발생해야_한다() throws Exception {
        // Given: 이미 쿠폰을 발급받은 회원이 있고
        CouponIssueRequest request = new CouponIssueRequest(1L);

        when(couponService.issue(1L))
            .thenThrow(new RuntimeException("이미 쿠폰을 발급받았습니다"));

        // When & Then: 쿠폰 발급을 요청하면
        mockMvc.perform(post("api/coupons/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
    }
}