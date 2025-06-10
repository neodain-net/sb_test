package com.neodain.springbootbatchdemo.service.mapper;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.neodain.springbootbatchdemo.dto.MemberDto.MemberRequest;
import com.neodain.springbootbatchdemo.dto.MemberDto.MemberResponse;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;

public class DevopsMemberMapper {
  // This class can be used to map between DevopsMember JPO and Member DTOs.
  // For example, you can use ModelMapper or MapStruct to implement the mapping
  // logic.

  // DevopsMemberRequest → DevopsMember 변환 : DTO → 도메인 모델 변환
  // public DevopsMember toEntity(DevopsMemberRequest request) {
  // return DevopsMember.builder()
  // .name(request.name())
  // .nickname(request.nickname())
  //
  // DTO → Entity 변환
  public DevopsMember toEntity(MemberRequest request) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
    Date utilDate = null; // java.util.Date to hold the parsed date
    try {
      utilDate = dateFormat.parse(request.birthday()); // Parse the date string
    } catch (ParseException e) {
      throw new RuntimeException("Invalid date format: " + request.birthday(), e); // Wrap the exception
    }

    Date date = new java.sql.Date(utilDate.getTime()); // Convert java.util.Date to java.sql.Date

    return DevopsMember.builder()
        // .memberId(request.getMemberId()) // Use the correct getter method
        .name(request.name())
        .nickname(request.nickname())
        .gender(DevopsMember.Gender.valueOf(request.gender().toLowerCase())) // String → Enum 변환
        .birthday(date)
        .phoneNum(request.phoneNum())
        .email(request.email())
        .build();
  }

  // Entity → DTO 변환
  public MemberResponse toDto(DevopsMember entity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = entity.getBirthday() != null ? dateFormat.format(entity.getBirthday()) : null;

    return new MemberResponse(
        entity.getMemberId(), // Assuming memberId is already a String
        entity.getName(),
        entity.getNickname(),
        entity.getGender().name(), // Enum → String 변환
        formattedDate,
        // new SimpleDateFormat("yyyy-MM-dd").format(entity.getBirthday()), // Format date to String
        entity.getPhoneNum(),
        entity.getEmail());
  }
}
