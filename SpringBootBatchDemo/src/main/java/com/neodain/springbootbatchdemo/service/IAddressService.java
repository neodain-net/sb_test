package com.neodain.springbootbatchdemo.service;

import java.util.List;

import com.neodain.springbootbatchdemo.dto.AddressDto.AddressRequest;
import com.neodain.springbootbatchdemo.dto.AddressDto.AddressResponse;

public interface IAddressService {
  AddressResponse create(String memberId, AddressRequest request);
  AddressResponse get(Long id);
  List<AddressResponse> getAll();
  AddressResponse update(Long id, AddressRequest request);
  void delete(Long id);
}
