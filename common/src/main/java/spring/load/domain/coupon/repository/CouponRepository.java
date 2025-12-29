package spring.load.domain.coupon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import spring.load.domain.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
    Optional<Coupon> findByCouponCode(String CouponCode);
    List<Coupon> findByMemberId(Long memberId);
    
    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.member.id = :memberId AND c.event.id = :eventId")
    Long countByMemberIdAndEventId(@Param("memberId") Long memberId, @Param("eventId") Long eventId);
}

