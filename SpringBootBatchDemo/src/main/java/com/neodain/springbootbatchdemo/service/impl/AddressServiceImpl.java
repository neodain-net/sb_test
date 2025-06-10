package com.neodain.springbootbatchdemo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.AddressDto.AddressRequest;
import com.neodain.springbootbatchdemo.dto.AddressDto.AddressResponse;
import com.neodain.springbootbatchdemo.service.IAddressService;
import com.neodain.springbootbatchdemo.store.jpo.Address;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;
import com.neodain.springbootbatchdemo.store.repository.IAddressRepository;
import com.neodain.springbootbatchdemo.store.repository.ICityRepository;
import com.neodain.springbootbatchdemo.store.repository.IDevopsMemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements IAddressService {

    private final IAddressRepository repository;
    private final IDevopsMemberRepository memberRepository;
    private final ICityRepository cityRepository;

    @Override
    public AddressResponse create(String memberId, AddressRequest request) {
        DevopsMember member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return null;
        }
        Address address = Address.builder()
                .member(member)
                .type(Address.AddressType.fromString(request.addressType().toLowerCase()))
                .street(request.street())
                .addressLine(request.addressLine())
                .zipCode(request.zipCode())
                .city(request.cityId() != null ? cityRepository.findById(request.cityId()).orElse(null) : null)
                .build();
        repository.save(address);
        return toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse get(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse update(Long id, AddressRequest request) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setType(Address.AddressType.valueOf(request.addressType().toLowerCase()));
                    entity.setStreet(request.street());
                    entity.setAddressLine(request.addressLine());
                    entity.setZipCode(request.zipCode());
                    entity.setCity(request.cityId() != null ? cityRepository.findById(request.cityId()).orElse(null) : null);
                    return toResponse(repository.save(entity));
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private AddressResponse toResponse(Address address) {
        Long cityId = address.getCity() != null ? address.getCity().getCityId() : null;
        return new AddressResponse(
                address.getId(),
                address.getType().name(), // Enum → String 변환
                address.getStreet(),
                address.getAddressLine(),
                address.getZipCode(),
                cityId);
    }
}