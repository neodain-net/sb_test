package com.neodain.springbootbatchdemo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.DevopsDto.DevopsRequest;
import com.neodain.springbootbatchdemo.dto.DevopsDto.DevopsResponse;
import com.neodain.springbootbatchdemo.exception.AlreadyExistsException;
import com.neodain.springbootbatchdemo.exception.NotFoundException;
import com.neodain.springbootbatchdemo.service.IDevopsService;
import com.neodain.springbootbatchdemo.store.jpo.Devops;
import com.neodain.springbootbatchdemo.store.repository.IDevopsRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsServiceImpl implements IDevopsService {

    private final IDevopsRepository repository;

    @Override
    public DevopsResponse create(DevopsRequest request) {
        if (repository.existsByName(request.name())) {
            throw new AlreadyExistsException("Devops already exists : " + request.name());
        }
        Devops devops = Devops.builder()
                .devopsId(UUID.randomUUID().toString())
                .name(request.name())
                .intro(request.intro())
                .foundationTime(LocalDateTime.now())
                .build();
        repository.save(devops);
        return toResponse(devops);
    }

    @Override
    @Transactional(readOnly = true)
    public DevopsResponse get(String devopsId) {
        return repository.findById(devopsId)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("DevOps not found with ID: " + devopsId));
        // If you want to return null instead of throwing an exception, you can use:
        // return repository.findById(devopsId).map(this::toResponse).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DevopsResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DevopsResponse update(String devopsId, DevopsRequest request) {
        return repository.findById(devopsId)
                .map(entity -> {
                    entity.setName(request.name());
                    entity.setIntro(request.intro());
                    return toResponse(repository.save(entity));
                }).orElseThrow(() -> new NotFoundException("DevOps not found with ID: " + devopsId));
    }

    @Override
    public void delete(String devopsId) {
        repository.deleteById(devopsId);
    }

    private DevopsResponse toResponse(Devops devops) {
        return new DevopsResponse(
                devops.getDevopsId(),
                devops.getName(),
                devops.getIntro(),
                devops.getFoundationTime());
    }
}