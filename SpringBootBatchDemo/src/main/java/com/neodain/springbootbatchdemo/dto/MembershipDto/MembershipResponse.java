package com.neodain.springbootbatchdemo.dto.MembershipDto;

import java.time.LocalDateTime;

public record MembershipResponse(
    Long id,
    String memberId,
    String name,
    String email,
    String phoneNum,
    String role,
    LocalDateTime joinDate) {}