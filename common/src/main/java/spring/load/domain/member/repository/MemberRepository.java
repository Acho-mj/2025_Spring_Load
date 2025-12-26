package spring.load.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.load.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
    
}

