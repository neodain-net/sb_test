package com.neodain.springbootbatchdemo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.AddressDto.AddressRequest;
import com.neodain.springbootbatchdemo.dto.AddressDto.AddressResponse;
import com.neodain.springbootbatchdemo.exception.AlreadyExistsException;
import com.neodain.springbootbatchdemo.exception.NotFoundException;
import com.neodain.springbootbatchdemo.service.IAddressService;
import com.neodain.springbootbatchdemo.store.jpo.Address;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;
import com.neodain.springbootbatchdemo.store.repository.IAddressRepository;
import com.neodain.springbootbatchdemo.store.repository.IDevopsMemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements IAddressService {

    private final IAddressRepository repository;
    private final IDevopsMemberRepository memberRepository;

    @Override
    public AddressResponse create(String memberId, AddressRequest request) {
        DevopsMember member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + memberId));
        // If member is not found, throw NotFoundException
        // This will be caught by the controller and return a 404 response
        // If you want to return null instead of throwing an exception, you can change
        // this logic
        // to return null or handle it differently, but it's generally better to throw
        // an exception
        // to indicate that the requested resource does not exist.
        // as it provides clearer feedback to the client.

        Address.AddressType type = Address.AddressType.fromString(request.addressType().toLowerCase());
        if (repository.existsByMemberAndType(member, type)) { 
            // If an address of the same type already exists for the member, throw AlreadyExistsException
            // This will be caught by the controller and return a 409 response
            throw new AlreadyExistsException("Address already exists for member ID: " + memberId +
                    " with type: " + request.addressType());
        }

        Address address = Address.builder()
                .member(member)
                .type(type)
                .street(request.street())
                .addressLine(request.addressLine())
                .zipCode(request.zipCode())
                .city(request.city())
                .build();
        repository.save(address);
        return toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse get(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Address not found with ID: " + id));
        // If you want to return null instead of throwing an exception, you can use:
        // return repository.findById(id).map(this::toResponse)
        // 혹은 .orElse(null);
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
                    return toResponse(repository.save(entity));
                }).orElseThrow(() -> new NotFoundException("Address not found with ID: " + id));
        // If you want to return null instead of throwing an exception, you can use:
        // return repository.findById(id)
        // .map(entity -> {
        // entity.setType(Address.AddressType.valueOf(request.addressType().toLowerCase()));
        // entity.setStreet(request.street());
        // entity.setAddressLine(request.addressLine());
        // entity.setZipCode(request.zipCode());
        // return toResponse(repository.save(entity));
        // })
        // 혹은 .orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getType().name(), // Enum → String 변환
                address.getCity(),
                address.getStreet(),
                address.getAddressLine(),
                address.getZipCode());
    }
}