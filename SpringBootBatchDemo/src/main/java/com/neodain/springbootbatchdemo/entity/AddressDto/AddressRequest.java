package com.neodain.springbootbatchdemo.entity.AddressDto;

import com.neodain.springbootbatchdemo.entity.Address.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
  @NotNull 
  AddressType addressType,
  @NotBlank 
  String addressLine,
  String city,
  String state,
  String zipCode
) {}
