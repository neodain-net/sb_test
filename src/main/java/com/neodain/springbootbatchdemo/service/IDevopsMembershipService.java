package com.neodain.springbootbatchdemo.service;

import java.util.List;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.entity.DevopsMembership.DevopsMembershipId;

public interface IDevopsMembershipService {
  MembershipResponse create(MembershipRequest request);
  MembershipResponse get(DevopsMembershipId id);
  List<MembershipResponse> getAll();
  MembershipResponse update(DevopsMembershipId id, MembershipRequest request);
  void delete(DevopsMembershipId id);
}
