package com.neodain.springbootbatchdemo.dto.AddressDto;

import com.neodain.springbootbatchdemo.store.jpo.Address.AddressType;

public record AddressResponse(
    Long id,
    AddressType addressType,
    String street,
    String addressLine,
    String zipCode,
    Long cityId) {
}
