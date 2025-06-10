
package com.neodain.springbootbatchdemo.dto.MemberDto;

public record MemberResponse(
    String memberId,
    String name,
    String nickname,
    String gender,
    String birthDay,
    String phoneNum,
    String email) {}
