
package com.neodain.springbootbatchdemo.dto.MemberDto;

import com.neodain.springbootbatchdemo.store.jpo.DevopsMember.Gender;

public record MemberResponse(
    String memberId,
    String name,
    String nickname,
    Gender gender,
    String birthDay,
    String phoneNum,
    String email) {
}
