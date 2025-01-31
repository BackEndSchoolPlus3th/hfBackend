package com.ll.hfback.global.oauth2;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;


@Component
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

  private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;


  public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
    this.defaultResolver =
        new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, "/oauth2/authorization"
        );
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
    return _customizeAuthorizationRequest(authorizationRequest, request);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
    return _customizeAuthorizationRequest(authorizationRequest, request);
  }

  private OAuth2AuthorizationRequest _customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
    if (authorizationRequest == null || request == null) {
      return null;
    }

    String redirectUrl = request.getParameter("redirectUrl");
    if (redirectUrl != null && !redirectUrl.isEmpty()) {
      System.out.println("Redirect URL: " + redirectUrl);  // 로그 추가
    } else {
      System.out.println("No Redirect URL found");
    }


    Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());
    if (redirectUrl != null && !redirectUrl.isEmpty()) {
      additionalParameters.put("state", redirectUrl);
    }

    return OAuth2AuthorizationRequest.from(authorizationRequest)
        .additionalParameters(additionalParameters)
        .state(redirectUrl)
        .build();
  }
}
