package com.neodain.springbootbatchdemo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMembership;

public interface IDevopsMembershipRepository extends JpaRepository<DevopsMembership, Long> {
}