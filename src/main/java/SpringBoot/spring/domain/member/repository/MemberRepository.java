package SpringBoot.spring.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringBoot.spring.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
    
}
