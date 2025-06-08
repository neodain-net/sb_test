
package com.neodain.springbootbatchdemo.dto.MemberDto;

import com.neodain.springbootbatchdemo.store.jpo.DevopsMember.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberRequest(
    @NotBlank(message = "이름은 필수입니다.") String name,
    String nickname,
    Gender gender,
    String birthday,
    @NotBlank @Pattern(regexp = "^\\d{10,13}$") String phoneNum,
    @NotBlank @Email String email) {
}
