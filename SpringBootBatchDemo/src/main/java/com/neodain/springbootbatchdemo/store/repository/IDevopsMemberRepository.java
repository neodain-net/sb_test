package com.neodain.springbootbatchdemo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;

public interface IDevopsMemberRepository extends JpaRepository<DevopsMember, String> {
  boolean existsByPhoneNum(String phoneNum);

  boolean existsByEmail(String email);
}