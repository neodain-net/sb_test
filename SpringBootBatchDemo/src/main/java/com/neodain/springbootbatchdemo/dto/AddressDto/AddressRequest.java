package com.neodain.springbootbatchdemo.dto.AddressDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
    // @NotNull AddressType addressType,
    @NotNull String addressType, // DTO는 도메인 모델과(JPO) 분리되어야 하므로 AddressType 대신 String 사용
    @NotBlank String city,
    @NotBlank String street,
    String addressLine,
    String zipCode) {}
