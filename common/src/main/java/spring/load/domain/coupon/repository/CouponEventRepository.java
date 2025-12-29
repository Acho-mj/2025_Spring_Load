package spring.load.domain.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import spring.load.domain.coupon.entity.CouponEvent;

public interface CouponEventRepository extends JpaRepository<CouponEvent, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM CouponEvent e WHERE e.id = :id")
    Optional<CouponEvent> findByIdWithLock(@Param("id") Long id);
    
    Optional<CouponEvent> findByEventName(String eventName);
}

