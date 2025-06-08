package com.neodain.springbootbatchdemo.dto.MembershipDto;

import jakarta.validation.constraints.NotBlank;

public record MembershipRequest(
    @NotBlank(message = "memberId 는 필수 필드입니다.") String memberId,
    @NotBlank(message = "devopsId 는 필수 필드입니다.") String devopsId) {
}