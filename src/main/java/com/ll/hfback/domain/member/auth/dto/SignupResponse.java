package com.ll.hfback.domain.member.auth.dto;

import com.ll.hfback.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class SignupResponse {
    @NonNull
    private final Long id;
    @NonNull
    private final String email;
    @NonNull
    private final String nickname;
    private final Member.Gender gender;
    @NonNull
    private final LocalDateTime createDate;
    private final String loginType;

    public SignupResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.createDate = member.getCreateDate();
        this.loginType = member.getLoginType();
    }

}
