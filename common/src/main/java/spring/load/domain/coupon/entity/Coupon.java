package spring.load.domain.coupon.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.load.domain.member.entity.Member;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="event_id", nullable=false)
    private CouponEvent event;

    @Column(unique=true, nullable=false)
    private String couponCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private LocalDateTime issuedAt;
    
    private LocalDateTime usedAt;
    
    private LocalDateTime expiredAt;
}

