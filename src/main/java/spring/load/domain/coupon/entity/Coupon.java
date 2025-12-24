package spring.load.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.load.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "coupon", uniqueConstraints = {
    @UniqueConstraint(columnNames = "couponCode")
})
public class Coupon {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;    

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id", nullable=false)
    private Member member;

    @Column(unique=true, nullable=false)
    private String couponCode;

    @Column(nullable = false)
    private Long discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private LocalDateTime issuedAt;
    
    private LocalDateTime usedAt;
    
    private LocalDateTime expiredAt;
}
