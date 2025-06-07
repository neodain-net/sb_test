package com.neodain.springbootbatchdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.entity.DevopsMember;

public interface IDevopsMemberRepository extends JpaRepository<DevopsMember, String> {
}