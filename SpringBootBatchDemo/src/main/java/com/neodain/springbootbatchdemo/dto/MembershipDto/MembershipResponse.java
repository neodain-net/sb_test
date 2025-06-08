package com.neodain.springbootbatchdemo.entity.MembershipDto;

import java.time.LocalDateTime;
import com.neodain.springbootbatchdemo.entity.DevopsMembership.Role;

public record MembershipResponse(
    Long id,
    String memberId,
    String name,
    String email,
    String phoneNum,
    Role role,
    LocalDateTime joinDate) {
}