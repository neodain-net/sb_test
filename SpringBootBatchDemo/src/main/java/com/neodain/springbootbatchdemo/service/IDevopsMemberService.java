package com.neodain.springbootbatchdemo.service;

import java.util.List;

import com.neodain.springbootbatchdemo.dto.MemberDto.MemberRequest;
import com.neodain.springbootbatchdemo.dto.MemberDto.MemberResponse;

public interface IDevopsMemberService {
  MemberResponse create(MemberRequest request);
  MemberResponse get(String memberId);
  List<MemberResponse> getAll();
  MemberResponse update(String memberId, MemberRequest request);
  void delete(String memberId);
}
