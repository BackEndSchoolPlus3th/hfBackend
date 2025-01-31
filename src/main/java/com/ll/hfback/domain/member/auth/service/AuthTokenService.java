package com.ll.hfback.domain.member.auth.service;


import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.standard.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {

  @Value("${custom.jwt.secretKey}")
  private String jwtSecretKey;

  @Value("${custom.accessToken.expirationSeconds}")
  private long accessTokenExpirationSeconds;


  String genAccessToken(Member member) {
    long id = member.getId();
    String email = member.getEmail();
    String nickname = member.getNickname();
    String profilePath = member.getProfilePath();
    String role = member.getRole().name();

    return Ut.jwt.toString(
        jwtSecretKey,
        accessTokenExpirationSeconds,
        Map.of(
            "id", id, "email", email, "nickname", nickname,
            "profilePath", profilePath, "role", role
        )
    );
  }


  Map<String, Object> payload(String accessToken) {
    Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);
    if (parsedPayload == null) {
      return null;
    }
    long id = (long) (Integer) parsedPayload.get("id");
    String email = (String) parsedPayload.get("email");
    String nickname = (String) parsedPayload.get("nickname");
    String profilePath = (String) parsedPayload.get("profilePath");
    String role = (String) parsedPayload.get("role");

    return Map.of(
        "id", id, "email", email, "nickname", nickname,
        "profilePath", profilePath, "role", role
    );
  }
}
