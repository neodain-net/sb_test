package com.neodain.springbootbatchdemo.entity.AddressDto;

import com.neodain.springbootbatchdemo.entity.Address.AddressType;

public record AddressResponse(
    Long id,
    AddressType addressType,
    String street,
    String addressLine,
    String zipCode,
    Long cityId) {
}
