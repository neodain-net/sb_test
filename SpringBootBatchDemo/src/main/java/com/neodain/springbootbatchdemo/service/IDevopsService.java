package com.neodain.springbootbatchdemo.service;

import java.util.List;

import com.neodain.springbootbatchdemo.dto.DevopsDto.DevopsRequest;
import com.neodain.springbootbatchdemo.dto.DevopsDto.DevopsResponse;

public interface IDevopsService {
  DevopsResponse create(DevopsRequest request);
  DevopsResponse get(String devopsId);
  List<DevopsResponse> getAll();
  DevopsResponse update(String devopsId, DevopsRequest request);
  void delete(String devopsId);
}