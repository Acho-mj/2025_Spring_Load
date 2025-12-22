package spring.load.unit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import spring.load.domain.coupon.entity.Coupon;
import spring.load.domain.coupon.entity.CouponStatus;
import spring.load.domain.coupon.repository.CouponRepository;
import spring.load.domain.member.entity.Member;
import spring.load.domain.member.repository.MemberRepository;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Coupon Repository 테스트")
public class CouponRepositoryTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }
    
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;


    @BeforeEach
    void setUp(){
        testMember = createTestMember();
        testMember = memberRepository.save(testMember);
    }
    private Member createTestMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setName("테스트 유저1");
        return member;
    }
    private Coupon createCoupon(String couponCode, Member member, CouponStatus status) {
        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponCode);
        coupon.setMember(member);
        coupon.setDiscountAmount(1000L);
        coupon.setStatus(status);
        coupon.setIssuedAt(LocalDateTime.now());
        return coupon;
    }

    @Test
    @DisplayName("회원 ID로 발급받은 쿠폰을 조회할 수 있다")
    void 회원ID로_발급받은_쿠폰을_조회할_수_있다() {
        // Given: 회원이 쿠폰을 발급받았고
        Coupon coupon = createCoupon("coupon-001", testMember, CouponStatus.ISSUED);
        couponRepository.save(coupon);

        // When: 회원 ID로 조회하면
        List<Coupon> memberCoupon = couponRepository.findByMemberId(testMember.getId());

        // Then: 발급받은 쿠폰이 조회되어야 한다
        assertThat(memberCoupon).hasSize(1);
        assertThat(memberCoupon.get(0).getCouponCode()).isEqualTo("coupon-001");
    }

    @Test
    @DisplayName("쿠폰 코드로 쿠폰을 조회할 수 있다")
    void 쿠폰코드로_쿠폰을_조회할_수_있다() {
        // Given: 발급된 쿠폰이 있고
        String couponCode = "coupon-002";
        Coupon coupon = createCoupon(couponCode, testMember, CouponStatus.ISSUED);
        couponRepository.save(coupon);

        // When: 쿠폰 코드로 조회하면
        Optional<Coupon> foundCoupon = couponRepository.findByCouponCode(couponCode);
        
        // Then: 해당 쿠폰이 조회되어야 한다
        assertThat(foundCoupon).isPresent();
        assertThat(foundCoupon.get().getCouponCode()).isEqualTo(couponCode);
        assertThat(foundCoupon.get().getMember().getId()).isEqualTo(testMember.getId());
    }
    
    @Test
    @DisplayName("중복된 쿠폰 코드는 저장할 수 없다")
    void 중복된_쿠폰코드는_저장할_수_없다() {
        // Given: 이미 저장된 쿠폰 코드가 있고
        String duplicateCouponCode = "coupon-003";
        Coupon firstCoupon = createCoupon(duplicateCouponCode, testMember, CouponStatus.ISSUED);
        couponRepository.saveAndFlush(firstCoupon);

        Coupon secondCoupon = createCoupon(duplicateCouponCode, testMember, CouponStatus.ISSUED);

        // When: 동일한 쿠폰 코드로 저장을 시도하면
        // Then: 예외가 발생해야 한다
        assertThatThrownBy(() -> couponRepository.saveAndFlush(secondCoupon))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}
