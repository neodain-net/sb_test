package com.neodain.springbootbatchdemo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.store.jpo.Devops;

public interface IDevopsRepository extends JpaRepository<Devops, String> {
}