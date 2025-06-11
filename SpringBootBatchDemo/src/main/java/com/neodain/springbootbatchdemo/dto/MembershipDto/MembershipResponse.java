package com.neodain.springbootbatchdemo.dto.MembershipDto;

import java.time.LocalDateTime;

public record MembershipResponse(
    Long id,
    String memberId,
    String devopsId,
    String role,
    LocalDateTime joinDate) {}