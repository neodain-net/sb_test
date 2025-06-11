package com.neodain.springbootbatchdemo.service.mapper;

import com.neodain.springbootbatchdemo.dto.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.dto.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.store.jpo.Devops;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMembership;
import com.neodain.springbootbatchdemo.store.repository.IDevopsMemberRepository;
import com.neodain.springbootbatchdemo.store.repository.IDevopsRepository;

public class DevopsMembershipMapper {

  // DTO → Entity 변환
  public static DevopsMembership toEntity(MembershipRequest request, IDevopsMemberRepository memberRepository,
      IDevopsRepository devopsRepository) {
    DevopsMember member = memberRepository.findById(request.memberId())
        .orElseThrow(() -> new RuntimeException("Member not found with ID: " + request.memberId()));

    Devops devops = devopsRepository.findById(request.devopsId())
        .orElseThrow(() -> new RuntimeException("Devops not found with ID: " + request.devopsId()));

    return DevopsMembership.builder()
        .member(member)
        .devops(devops)
        .build();
  }

  // Entity → DTO 변환
  public static MembershipResponse toDto(DevopsMembership entity) {
    return new MembershipResponse(
      entity.getId(),
      entity.getMember().getMemberId(),
      entity.getDevops().getDevopsId(),
      entity.getRole().name(), // Enum → String 변환
      entity.getJoinDate());
  }
}
