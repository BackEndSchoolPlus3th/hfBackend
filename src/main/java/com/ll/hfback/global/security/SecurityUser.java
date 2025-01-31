package com.ll.hfback.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class SecurityUser extends User implements OAuth2User {
  @Getter
  private long id;
  @Getter
  private String nickname;
  @Getter
  private String profilePath;


  public SecurityUser(
      long id, String email, String password, String nickname, String profilePath,
      Collection<? extends GrantedAuthority> authorities) {
    super(email, password != null ? password : "", authorities);
    this.id = id;
    this.nickname = nickname;
    this.profilePath = profilePath;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of(
        "id", id,
        "email", getUsername(),
        "nickname", nickname,
        "profilePath", profilePath
    );
  }

  @Override
  public String getName() {
    return getUsername();
  }
}
