package spring.load.domain.coupon.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coupon_event")
public class CouponEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventName;

    @Column(nullable = false)
    private Integer totalStock; // 총 재고

    @Column(nullable = false)
    private Integer remainingStock; // 남은 재고

    @Column(nullable = false)
    private LocalDateTime startAt; // 이벤트 시작 시간

    @Column(nullable = false)
    private LocalDateTime endAt; // 이벤트 종료 시간

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (remainingStock == null) {
            remainingStock = totalStock;
        }
    }

    public boolean isAvailable() {
        LocalDateTime now = LocalDateTime.now();
        return remainingStock > 0 && 
               now.isAfter(startAt) && 
               now.isBefore(endAt);
    }

    public boolean decreaseStock() {
        if (remainingStock <= 0) {
            return false;
        }
        remainingStock--;
        return true;
    }
}

