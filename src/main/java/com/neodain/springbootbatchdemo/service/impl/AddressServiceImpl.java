package com.neodain.springbootbatchdemo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.neodain.springbootbatchdemo.entity.Address;
import com.neodain.springbootbatchdemo.entity.AddressDto.AddressRequest;
import com.neodain.springbootbatchdemo.entity.AddressDto.AddressResponse;
import com.neodain.springbootbatchdemo.entity.DevopsMember;
import com.neodain.springbootbatchdemo.repository.IAddressRepository;
import com.neodain.springbootbatchdemo.repository.IDevopsMemberRepository;
import com.neodain.springbootbatchdemo.service.IAddressService;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements IAddressService {

    private final IAddressRepository repository;
    private final IDevopsMemberRepository memberRepository;

    @Override
    public AddressResponse create(String memberId, AddressRequest request) {
        DevopsMember member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return null;
        }
        Address address = Address.builder()
                .member(member)
                .type(request.addressType())
                .addressLine(request.addressLine())
                .city(request.city())
                .state(request.state())
                .zipCode(request.zipCode())
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
                    entity.setType(request.addressType());
                    entity.setAddressLine(request.addressLine());
                    entity.setCity(request.city());
                    entity.setState(request.state());
                    entity.setZipCode(request.zipCode());
                    return toResponse(repository.save(entity));
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getType(),
                address.getAddressLine(),
                address.getCity(),
                address.getState(),
                address.getZipCode());
    }
}