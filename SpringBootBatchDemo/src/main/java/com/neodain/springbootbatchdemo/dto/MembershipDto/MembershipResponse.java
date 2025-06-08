package com.neodain.springbootbatchdemo.dto.MembershipDto;

import java.time.LocalDateTime;

import com.neodain.springbootbatchdemo.store.jpo.DevopsMembership.Role;

public record MembershipResponse(
    Long id,
    String memberId,
    String name,
    String email,
    String phoneNum,
    Role role,
    LocalDateTime joinDate) {
}