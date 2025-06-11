package com.neodain.springbootbatchdemo.dto.AddressDto;

public record AddressResponse(
    Long id,
    // AddressType addressType,
    String addressType, // DTO는 도메인 모델과(JPO) 분리되어야 하므로 AddressType 대신 String 사용
    String city,
    String street,
    String addressLine,
    String zipCode) {}