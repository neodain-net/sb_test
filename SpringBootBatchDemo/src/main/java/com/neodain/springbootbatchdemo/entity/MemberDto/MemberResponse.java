
package com.neodain.springbootbatchdemo.entity.MemberDto;

import com.neodain.springbootbatchdemo.entity.DevopsMember.Gender;

public record MemberResponse(
  String memberId,
  String name,
  String nickname,
  Gender gender,
  String birthDay,
  String phoneNum,
  String email
) {}
