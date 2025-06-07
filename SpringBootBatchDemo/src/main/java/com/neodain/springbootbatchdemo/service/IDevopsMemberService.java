package com.neodain.springbootbatchdemo.service;

import java.util.List;

import com.neodain.springbootbatchdemo.entity.MemberDto.MemberResponse;
import com.neodain.springbootbatchdemo.entity.MemberDto.MemberRequest;

public interface IDevopsMemberService {
  MemberResponse create(MemberRequest request);
  MemberResponse get(String memberId);
  List<MemberResponse> getAll();
  MemberResponse update(String memberId, MemberRequest request);
  void delete(String memberId);
}
