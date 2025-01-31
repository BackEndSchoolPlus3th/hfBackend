package com.ll.hfback.domain.member.auth.repository;

import com.ll.hfback.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Member, Long> {
  Member findByEmail(String email);

  Optional<Member> findByApiKey(String apiKey);
}
