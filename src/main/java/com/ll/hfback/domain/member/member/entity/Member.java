package com.ll.hfback.domain.member.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.hfback.domain.member.alert.entity.Alert;
import com.ll.hfback.domain.member.auth.entity.SocialAccount;
import com.ll.hfback.domain.member.member.dto.MemberUpdateRequest;
import com.ll.hfback.domain.member.report.entity.Report;
import com.ll.hfback.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.UtilityClass;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DynamicInsert
public class Member extends BaseEntity {

    @Column(unique = true, nullable = false, length = 30)
    private String email;


    @JsonIgnore
    private String password;

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }


    @Column(nullable = false)
    private String nickname;  // 닉네임


    private LocalDate birthday;
    private String location;  // 주소


    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;  // 성별
    @Getter
    public enum Gender {
        M, W;
    }


    @Column(length = 20)
    private String phoneNumber;  // 전화번호


    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'default.png'")
    private String profilePath;  // 프로필 사진 경로


    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean mkAlarm;  // 마케팅 수신여부


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('NORMAL', 'DELETED', 'BANNED') DEFAULT 'NORMAL'")
    private MemberState state;  // 회원 상태
    @Getter
    public enum MemberState {
        NORMAL,  // 정상
        DELETED,  // 탈퇴
        BANNED  // 정지
    }

    public void deactivate() {
        state = MemberState.DELETED;
    }

    public void restore() {
        state = MemberState.NORMAL;
    }


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('ROLE_USER', 'ROLE_ADMIN') DEFAULT 'ROLE_USER'")
    private MemberRole role = MemberRole.ROLE_USER;  // 권한 (관리자, 사용자)
    @Getter
    public enum MemberRole {
        ROLE_USER, ROLE_ADMIN
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role != null) {
            return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority(MemberRole.ROLE_USER.name()));
        }
    }



    @JsonIgnore
    @Column(unique = true, length = 50)
    private String apiKey;


    @Column(nullable = false, columnDefinition = "VARCHAR(50) DEFAULT 'SELF'")
    private String loginType = LoginType.SELF;  // 최초 가입 방식
    @UtilityClass
    public class LoginType {
        public static final String SELF = "SELF";
        public static final String NAVER = "NAVER";
        public static final String KAKAO = "KAKAO";
        public static final String GOOGLE = "GOOGLE";

        public static boolean isValid(String loginType) {
            return SELF.equals(loginType) || NAVER.equals(loginType) || KAKAO.equals(loginType) || GOOGLE.equals(loginType);
        }
    }




    // 1대1 관계 설정
    @JsonIgnore
    @OneToOne(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private SocialAccount socialAccount;

    public SocialAccount getSocialAccountOrCreate() {
        if (socialAccount == null) {
            socialAccount = SocialAccount.builder()
                .member(this)
                .build();
        }
        return socialAccount;
    }





    // 1:N 관계 설정
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true) // fetch = FetchType.LAZY
    @Builder.Default
    private List<Alert> alerts = new ArrayList<>();

    public void addAlert(
        String content, String url, String category
    ) {
        Alert alert = Alert.builder()
            .member(this)
            .content(content)
            .url(url)
            .category(category)
            .build();
        alerts.add(alert);
    }

    public void removeAlert(Alert alert) {
        alerts.remove(alert);
    }


    @JsonIgnore
    @OneToMany(mappedBy = "reporter", cascade = ALL, orphanRemoval = true) // fetch = FetchType.LAZY
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);  // 수정 불가능 리스트 반환
    }

    public void addReport(Report report) { reports.add(report); }

    public void removeReport(Report report) {
        reports.remove(report);
    }




    // Entity 메서드
    public Member(long id, String email, String nickname, String profilePath, MemberRole role) {
        this.setId(id);
        this.email = email;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.role = role;
    }

    public void updateInfo(MemberUpdateRequest request) {
        email = request.getEmail();
        gender = request.getGender();
        nickname = request.getNickname();
        password = request.getPassword();
        phoneNumber = request.getPhoneNumber();
        mkAlarm = request.isMkAlarm();
        birthday = request.getBirthday();
        location = request.getLocation();
        profilePath = request.getProfilePath();
    }

}
