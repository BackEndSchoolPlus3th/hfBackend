package com.ll.hfback.global.security;

import com.ll.hfback.domain.member.auth.service.AuthService;
import com.ll.hfback.global.rsData.RsData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final HttpServletRequest req;
  private final HttpServletResponse resp;
  private final AuthService authService;

  @Override
  @SneakyThrows
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) {
    if (request.getRequestURI().equals("/api/v1/auth/login") || request.getRequestURI()
        .equals("/api/v1/auth/logout") || request.getRequestURI().equals("/api/v1/Posts/**")) {
      filterChain.doFilter(request, response);
      return;
    }

    Cookie[] cookies = req.getCookies();
    if (cookies == null || cookies.length == 0) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"message\": \"로그인이 필요합니다.\"}");
      return;
    }

    String accessToken = _getCookie("accessToken");
    // accessToken 검증 or refreshToken 발급
    if (!accessToken.isBlank()) {
      // 토큰 유효기간 검증
      if (!authService.validateToken(accessToken)) {
        String refreshToken = _getCookie("refreshToken");
        RsData<String> rs = authService.refreshAccessToken(refreshToken);
        _addHeaderCookie("accessToken", rs.getData());
      }
      // securityUser 가져오기
      SecurityUser securityUser = authService.getUserFromAccessToken(accessToken);
      // 인가 처리
      SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
    }
    filterChain.doFilter(request, response);
  }

  private String _getCookie(String name) {
    Cookie[] cookies = req.getCookies();
    return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(name)).findFirst()
        .map(Cookie::getValue).orElse("");
  }

  private void _addHeaderCookie(String tokenName, String token) {
    ResponseCookie cookie =
        ResponseCookie.from(tokenName, token).path("/").sameSite("None").secure(true).httpOnly(true)
            .build();
    resp.addHeader("Set-Cookie", cookie.toString());
  }

}
