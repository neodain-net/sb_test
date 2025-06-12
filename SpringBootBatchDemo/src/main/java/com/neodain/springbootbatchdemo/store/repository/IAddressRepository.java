package com.neodain.springbootbatchdemo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.store.jpo.Address;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;

public interface IAddressRepository extends JpaRepository<Address, Long> {
  /**
   * Checks if an address already exists for the given member and type.
   *
   * @param member the devops member
   * @param type   the address type
   * @return true if address already exists
   */
  boolean existsByMemberAndType(DevopsMember member, Address.AddressType type);
}