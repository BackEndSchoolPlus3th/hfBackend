package com.ll.hfback.global.webMvc;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.service.MemberService;
import com.ll.hfback.global.exceptions.ErrorCode;
import com.ll.hfback.global.exceptions.ServiceException;
import com.ll.hfback.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final MemberService memberService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginUser.class) &&
        parameter.getParameterType().equals(Member.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory
  ) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ServiceException(ErrorCode.UNAUTHORIZED);
    }

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    String email = securityUser.getName();

    return memberService.getMember(email);
  }
}
