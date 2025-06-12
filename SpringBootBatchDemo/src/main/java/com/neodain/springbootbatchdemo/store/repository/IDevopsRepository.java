package com.neodain.springbootbatchdemo.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.store.jpo.Devops;

public interface IDevopsRepository extends JpaRepository<Devops, String> {
  /**
   * Checks for existence of a Devops with the given name.
   *
   * @param name the Devops name
   * @return true if a Devops with the name exists
   */
  boolean existsByName(String name);
}