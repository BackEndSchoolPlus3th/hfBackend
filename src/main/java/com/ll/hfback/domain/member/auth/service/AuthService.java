package com.ll.hfback.domain.member.auth.service;

import com.ll.hfback.domain.member.auth.entity.SocialAccount;
import com.ll.hfback.domain.member.auth.repository.AuthRepository;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.entity.Member.LoginType;
import com.ll.hfback.domain.member.member.repository.MemberRepository;
import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthTokenService authTokenService;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    public Member signup(
            String email, String password, String nickname,
            String loginType, String providerId, String providerEmail, String profilePath
        ) {
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            throw new ServiceException(ErrorCode.DUPLICATE_EMAIL);
        }

        Member member = Member.builder()
                .email(email)
                .password(
                    loginType.equals(LoginType.SELF) ? passwordEncoder.encode(password) : null
                )
                .nickname(nickname)
                .loginType(loginType)
                .profilePath(profilePath)
                .apiKey(UUID.randomUUID().toString())
                .build();

        if (providerId != null) {
            _connectSocialAccount(member, loginType, providerId, providerEmail);
        }

        return memberRepository.save(member);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findByApiKey(String apiKey) {
        return authRepository.findByApiKey(apiKey);
    }

    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public String genAuthToken(Member member) {
        return member.getApiKey() + " " + genAccessToken(member);
    }

    public Member getMemberFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);
        if (payload == null) {
            return null;
        }
        long id = (long) payload.get("id");
        String email = (String) payload.get("email");
        String nickname = (String) payload.get("nickname");
        String profilePath = (String) payload.get("profilePath");
        String role = (String) payload.get("role");

        return new Member(id, email, nickname, profilePath, Member.MemberRole.valueOf(role));
    }

    public long count() {
        return memberRepository.count();
    }

    public Member modifyOrSignup(
        String email, String nickname,
        String loginType, String profilePath, String providerId
    ) {
        Optional<Member> existingMember = findByEmail(email);
        if (existingMember.isPresent()) {
            Member member = existingMember.get();
            _modify(member, nickname, loginType, providerId, email, profilePath);
            return member;
        }
        return signup(email, null, nickname, loginType, providerId, email, profilePath);
    }

    private void _modify(
            Member member, String nickname, String loginType,
            String providerId, String providerEmail, String profilePath
        ) {
        if (!LoginType.isValid(loginType)) {
            throw new ServiceException(ErrorCode.INVALID_LOGIN_TYPE);
        }

        _connectSocialAccount(member, loginType, providerId, providerEmail);

        member.setLoginType(loginType);
        member.setNickname(nickname);
        member.setProfilePath(profilePath);
    }

    private void _connectSocialAccount(
        Member member, String loginType,
        String providerId, String providerEmail
    ) {
        SocialAccount socialAccount = member.getSocialAccountOrCreate();

        if (LoginType.KAKAO.equals(loginType)) {
            socialAccount.connectKakao(providerId, providerEmail);

        } else if (LoginType.GOOGLE.equals(loginType)) {
            socialAccount.connectGoogle(providerId, providerEmail);
        }
    }

}
