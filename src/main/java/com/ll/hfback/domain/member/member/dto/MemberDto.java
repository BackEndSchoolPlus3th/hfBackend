package com.ll.hfback.domain.member.member.dto;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.entity.Member.Gender;
import com.ll.hfback.domain.member.member.entity.Member.MemberState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemberDto {
    @NotNull
    private final Long id;
    private final LocalDateTime createDate;
    @NotNull
    private final String nickname;
    private final LocalDate birthday;
    private final String location;
    private final Gender gender;
    private final String phoneNumber;
    private final String profilePath;
    private final MemberState state;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.birthday = member.getBirthday();
        this.location = member.getLocation();
        this.gender = member.getGender();
        this.phoneNumber = member.getPhoneNumber();
        this.profilePath = member.getProfilePath();
        this.state = member.getState();
        this.createDate = member.getCreateDate();
    }
}
