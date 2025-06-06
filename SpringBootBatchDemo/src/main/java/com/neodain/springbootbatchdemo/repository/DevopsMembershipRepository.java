package com.neodain.springbootbatchdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.entity.DevopsMembership;

public interface DevopsMembershipRepository extends JpaRepository<DevopsMembership, DevopsMembership.DevopsMembershipId> {
}
