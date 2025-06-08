package com.neodain.springbootbatchdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.entity.Devops;

public interface IDevopsRepository extends JpaRepository<Devops, String> {
}