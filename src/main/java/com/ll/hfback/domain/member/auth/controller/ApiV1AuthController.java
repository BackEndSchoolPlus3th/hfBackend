package com.ll.hfback.domain.member.auth.controller;

import com.ll.hfback.domain.member.auth.dto.LoginRequest;
import com.ll.hfback.domain.member.auth.dto.LoginResponse;
import com.ll.hfback.domain.member.auth.dto.SignupRequest;
import com.ll.hfback.domain.member.auth.dto.SignupResponse;
import com.ll.hfback.domain.member.auth.service.AuthService;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.service.MemberService;
import com.ll.hfback.global.jwt.JwtProvider;
import com.ll.hfback.global.rsData.RsData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ApiV1AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    // MEM01_LOGIN01 : 자체 로그인
//    @PostMapping("/login")
//    public RsData<Void> login(
//            @Valid @RequestBody LoginRequest request,
//            HttpServletResponse response
//        ) {
//        Member member = memberService.getMember(request.getEmail());
//        String token = jwtProvider.genAccessToken(member);
//
//        Cookie cookie = new Cookie("accessToken", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60);
//        response.addCookie(cookie);
//
//        String refreshToken = member.getRefreshToken();
//        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(60 * 60);
//        response.addCookie(refreshTokenCookie);
//
//        return new RsData<>("200", "로그인에 성공하였습니다.");
//    }


    // MEM01_LOGIN01 : 자체 로그인 - LoginResponse객체로 토큰 값을 리턴하도록 수정함
    @PostMapping("/login")
    public ResponseEntity<RsData<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        // 사용자 이메일로 멤버 조회
        Member member = memberService.getMember(request.getEmail());

        // Access Token 생성
        String token = jwtProvider.genAccessToken(member);

        // 쿠키에 accessToken 추가
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간 유효
        response.addCookie(cookie);

        // Refresh Token 가져오기
        String refreshToken = member.getRefreshToken();

        // Refresh Token을 쿠키에 추가
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60); // 1시간 유효
        response.addCookie(refreshTokenCookie);

        // LoginResponse 객체에 토큰 포함
        LoginResponse loginResponse = new LoginResponse(token, refreshToken);

        // 성공 응답 리턴
        return ResponseEntity.ok(new RsData<>("200", "로그인에 성공하였습니다.", loginResponse));
    }


    // MEM01_LOGIN02 : 구글 소셜 로그인
    //@PostMapping("/google")


    // MEM01_LOGIN03 : 아이디 저장 (이메일)
    //@PostMapping("/save-id")


    // MEM01_LOGIN04 : 아이디 찾기 (이메일)
    //@PostMapping("/find-id")


    // MEM01_LOGIN05 : 비밀번호 재설정
    //@PostMapping("/reset-password")


    // MEM02_SIGNUP01 : 자체 회원가입
    @PostMapping("/signup")
    public RsData<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        Member member = authService.signup(request);
        return new RsData<>("200", "회원가입에 성공하였습니다.", new SignupResponse(member));
    }

    // MEM02_SIGNUP02 : 이메일 인증
    //@PostMapping("/email-verify")


    // MEM02_SIGNUP03 : 도로명 주소 찾기
    //@PostMapping("/address-verify")


    // MEM06_LOGOUT : 로그아웃
    @GetMapping("/logout")
    public RsData<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        return new RsData<>("200", "로그아웃에 성공하였습니다.");
    }



}
