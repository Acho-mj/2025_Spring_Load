package spring.load.domain.coupon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.load.domain.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
    Optional<Coupon> findByCouponCode(String CouponCode);
    List<Coupon> findByMemberId(Long memberId);
}
