package com.ll.hfback.global.security;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.repository.MemberRepository;
import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_EMAIL));

    return new SecurityUser(
        member.getId(),
        member.getEmail(),
        member.getPassword(),
        member.getNickname(),
        member.getProfilePath(),
        member.getAuthorities()
    );
  }

}
