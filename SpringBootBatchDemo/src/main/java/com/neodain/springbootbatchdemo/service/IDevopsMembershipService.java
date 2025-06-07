package com.neodain.springbootbatchdemo.service;

import java.util.List;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipResponse;

public interface IDevopsMembershipService {
  MembershipResponse create(MembershipRequest request);
  MembershipResponse get(Long id);
  List<MembershipResponse> getAll();
  MembershipResponse update(Long id, MembershipRequest request);
  void delete(Long id);
}