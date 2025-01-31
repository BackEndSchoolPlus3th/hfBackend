package com.ll.hfback.domain.member.auth.entity;

import com.ll.hfback.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SocialAccount {
  @Id
  @OneToOne
  @JoinColumn(name = "member_id")
  @EqualsAndHashCode.Include
  private Member member;

  @Column(unique = true, length = 50)
  private String kakaoProviderId;

  @Column(unique = true, length = 50)
  private String kakaoEmail;

  @Column(nullable = false)
  @Builder.Default
  private boolean kakaoActive = false;

  @Setter(AccessLevel.PRIVATE)
  private LocalDateTime kakaoCreateDate;

  @Setter(AccessLevel.PRIVATE)
  private LocalDateTime kakaoModifyDate;

  @Column(length = 50, unique = true)
  private String googleProviderId;

  @Column(unique = true, length = 50)
  private String googleEmail;

  @Column(nullable = false)
  @Builder.Default
  private boolean googleActive = false;

  @Setter(AccessLevel.PRIVATE)
  private LocalDateTime googleCreateDate;

  @Setter(AccessLevel.PRIVATE)
  private LocalDateTime googleModifyDate;


  public void updateKakaoInfo(String providerId, String email, boolean active) {
    kakaoProviderId = providerId;
    kakaoEmail = email;
    kakaoActive = active;
    kakaoModifyDate = LocalDateTime.now();
  }

  public void connectKakao(String providerId, String email) {
    if (kakaoCreateDate == null) {
      kakaoCreateDate = LocalDateTime.now();
    }
    updateKakaoInfo(providerId, email, true);
  }

  public void disconnectKakao() {
    updateKakaoInfo(null, null, false);
  }

  public void updateGoogleInfo(String providerId, String email, boolean active) {
    googleProviderId = providerId;
    googleEmail = email;
    googleActive = active;
    googleModifyDate = LocalDateTime.now();
  }

  public void connectGoogle(String providerId, String email) {
    if (googleCreateDate == null) {
      googleCreateDate = LocalDateTime.now();
    }
    updateGoogleInfo(providerId, email, true);
  }

  public void disconnectGoogle() {
    updateGoogleInfo(null, null, false);
  }
}
