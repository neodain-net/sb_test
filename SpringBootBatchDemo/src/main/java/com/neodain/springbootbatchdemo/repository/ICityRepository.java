package com.neodain.springbootbatchdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neodain.springbootbatchdemo.entity.City;

public interface ICityRepository extends JpaRepository<City, Long> {
}