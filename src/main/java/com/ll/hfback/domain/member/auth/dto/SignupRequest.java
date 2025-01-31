package com.ll.hfback.domain.member.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
}
