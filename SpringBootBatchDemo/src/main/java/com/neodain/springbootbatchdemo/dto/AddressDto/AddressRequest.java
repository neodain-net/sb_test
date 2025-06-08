package com.neodain.springbootbatchdemo.dto.AddressDto;

import com.neodain.springbootbatchdemo.store.jpo.Address.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
    @NotNull AddressType addressType,
    @NotBlank String street,
    String addressLine,
    String zipCode,
    Long cityId) {
}
