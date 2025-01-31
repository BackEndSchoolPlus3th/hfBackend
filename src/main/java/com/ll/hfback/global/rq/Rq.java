package com.ll.hfback.global.rq;

import com.ll.hfback.domain.member.auth.service.AuthService;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.entity.Member.MemberRole;
import com.ll.hfback.global.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {

  private final HttpServletRequest req;
  private final HttpServletResponse resp;
  private final AuthService authService;


  public void setLogin(Member member) {
    UserDetails user = new SecurityUser(
        member.getId(),
        member.getEmail(),
        "",
        member.getNickname(),
        member.getProfilePath(),
        member.getAuthorities()
    );

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        user,
        user.getPassword(),
        user.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }


  public Member getActor() {
    return Optional.ofNullable(
            SecurityContextHolder
                .getContext()
                .getAuthentication()
        )
        .map(Authentication::getPrincipal)
        .filter(principal -> principal instanceof SecurityUser)
        .map(principal -> (SecurityUser) principal)
        .map(securityUser -> {
          String roleName = securityUser.getAuthorities()
              .stream()
              .findFirst()
              .map(GrantedAuthority::getAuthority)
              .orElse(MemberRole.ROLE_USER.name());
          MemberRole role = MemberRole.valueOf(roleName);

          return new Member(
              securityUser.getId(),
              securityUser.getUsername(),
              securityUser.getNickname(),
              securityUser.getProfilePath(),
              role
          );
        })
        .orElse(null);
  }


  public void setCookie(String name, String value) {
    ResponseCookie cookie = ResponseCookie.from(name, value)
        .path("/")
        .domain("localhost")
        .sameSite("Strict")
        .secure(true)
        .httpOnly(true)
        .build();
    resp.addHeader("Set-Cookie", cookie.toString());
  }


  public String getCookieValue(String name) {
    return Optional
        .ofNullable(req.getCookies())
        .stream() // 1 ~ 0
        .flatMap(cookies -> Arrays.stream(cookies))
        .filter(cookie -> cookie.getName().equals(name))
        .map(cookie -> cookie.getValue())
        .findFirst()
        .orElse(null);
  }


  public void deleteCookie(String name) {
    ResponseCookie cookie = ResponseCookie.from(name, null)
        .path("/")
        .domain("localhost")
        .sameSite("Strict")
        .secure(true)
        .httpOnly(true)
        .maxAge(0)
        .build();
    resp.addHeader("Set-Cookie", cookie.toString());
  }


  public void setHeader(String name, String value) {
    resp.setHeader(name, value);
  }


  public String getHeader(String name) {
    return req.getHeader(name);
  }


  public String makeAuthCookies(Member member) {
    String accessToken = authService.genAccessToken(member);
    setCookie("apiKey", member.getApiKey());
    setCookie("accessToken", accessToken);
    return accessToken;
  }
}
