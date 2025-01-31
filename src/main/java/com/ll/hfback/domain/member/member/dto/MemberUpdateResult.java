package com.ll.hfback.domain.member.member.dto;

import com.ll.hfback.domain.member.member.entity.Member;
import com.ll.hfback.domain.member.member.entity.Member.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class MemberUpdateResult {

    private String nickname;
    private String phoneNumber;
    private boolean mkAlarm;
    private Gender gender;
    private String profilePath;
    private String location;
    private LocalDate birthday;

    public static MemberUpdateResult of(Member member) {
        return MemberUpdateResult.builder().nickname(member.getNickname())
            .phoneNumber(member.getPhoneNumber()).mkAlarm(member.isMkAlarm())
            .profilePath(member.getProfilePath()).birthday(member.getBirthday())
            .gender(member.getGender()).location(member.getLocation()).build();
    }
}
