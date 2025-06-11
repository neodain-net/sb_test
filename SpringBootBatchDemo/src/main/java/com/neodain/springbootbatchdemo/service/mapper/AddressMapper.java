package com.neodain.springbootbatchdemo.service.mapper;

import com.neodain.springbootbatchdemo.dto.AddressDto.AddressRequest;
import com.neodain.springbootbatchdemo.dto.AddressDto.AddressResponse;
import com.neodain.springbootbatchdemo.store.jpo.Address;

public class AddressMapper {
  // This class can be used to map between Address JPO and Address DTOs.
  // For example, you can use ModelMapper or MapStruct to implement the mapping logic.

  // AddressRequest → Address 변환 : DTO → 도메인 모델 변환

  public Address toEntity(AddressRequest request) {
    return Address.builder()
        .type(Address.AddressType.fromString(request.addressType())) // String → Enum 변환
        .city(request.city())
        .street(request.street())
        .addressLine(request.addressLine())
        .zipCode(request.zipCode())
        .build();
  }

  // 도메인 모델 → DTO 변환
  public static AddressResponse toDto(Address entity) {
    return new AddressResponse(
        entity.getId(),
        entity.getType().name(), // Enum → String 변환
        entity.getCity(),
        entity.getStreet(),
        entity.getAddressLine(),
        entity.getZipCode());
  }
}
