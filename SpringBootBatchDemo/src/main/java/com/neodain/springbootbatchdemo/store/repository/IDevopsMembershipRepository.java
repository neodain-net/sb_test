package com.neodain.springbootbatchdemo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMembership;

public interface IDevopsMembershipRepository extends JpaRepository<DevopsMembership, Long> {
  /**
   * Checks if a membership already exists for the given devops and member.
   *
   * @param devopsId the devops identifier
   * @param memberId the member identifier
   * @return true if membership already exists
   */
  boolean existsByDevops_DevopsIdAndMember_MemberId(String devopsId, String memberId);
}