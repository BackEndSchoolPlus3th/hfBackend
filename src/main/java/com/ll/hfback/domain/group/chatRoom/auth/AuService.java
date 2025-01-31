package com.ll.hfback.domain.group.chatRoom.auth;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuService {
    private final MemberRepository memberRepository;

    public AuService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 이메일로 member 조회 및 반환
    public Member getMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }
        return member.get();
    }

    // 현재 로그인한 사용자의 고유 ID를 가져오는 메서드
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        // getName()에서 이메일을 가져온 후, MemberRepository를 통해 사용자 조회
        String email = authentication.getName(); // 사용자 이메일
        Member member = getMemberByEmail(email); // 이메일로 사용자 조회
        return member.getId(); // 사용자의 고유 ID 반환
    }

    // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        // getName()에서 이메일을 가져온 후, MemberRepository를 통해 사용자 조회
        String email = authentication.getName(); // 사용자 이메일
        Member member = getMemberByEmail(email); // 이메일로 사용자 조회
        return member; // 사용자의 member 객체 반환
    }
}
