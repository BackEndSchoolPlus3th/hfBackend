package com.ll.hfback.global.security;

import com.ll.hfback.domain.member.auth.service.AuthService;
import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter  extends OncePerRequestFilter {
  private final AuthService authService;
  private final Rq rq;
  record AuthTokens(String apiKey, String accessToken) {
  }


  private AuthTokens _getAuthTokensFromRequest() {
    String authorization = rq.getHeader("Authorization");
    if (authorization != null && authorization.startsWith("Bearer ")) {
      String token = authorization.substring("Bearer ".length());
      String[] tokenBits = token.split(" ", 2);
      if (tokenBits.length == 2) {
        return new AuthTokens(tokenBits[0], tokenBits[1]);
      }
    }

    String apiKey = rq.getCookieValue("apiKey");
    String accessToken = rq.getCookieValue("accessToken");
    if (apiKey != null && accessToken != null) {
      return new AuthTokens(apiKey, accessToken);
    }
    return null;
  }


  private void _refreshAccessToken(Member member) {
    String newAccessToken = authService.genAccessToken(member);
    rq.setHeader("Authorization", "Bearer " + member.getApiKey() + " " + newAccessToken);
    rq.setCookie("accessToken", newAccessToken);
  }


  private Member _refreshAccessTokenByApiKey(String apiKey) {
    Optional<Member> opMemberByApiKey = authService.findByApiKey(apiKey);
    if (opMemberByApiKey.isEmpty()) {
      return null;
    }
    Member member = opMemberByApiKey.get();
    _refreshAccessToken(member);
    return member;
  }


  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (!request.getRequestURI().startsWith("/api/")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (List.of("/api/v1/auth/login", "/api/v1/auth/logout", "/api/v1/auth/signup").contains(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    AuthTokens authTokens = _getAuthTokensFromRequest();
    if (authTokens == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String apiKey = authTokens.apiKey;
    String accessToken = authTokens.accessToken;

    Member member = authService.getMemberFromAccessToken(accessToken);
    if (member == null)
      member = _refreshAccessTokenByApiKey(apiKey);
    if (member != null)
      rq.setLogin(member);
    filterChain.doFilter(request, response);
  }

}
