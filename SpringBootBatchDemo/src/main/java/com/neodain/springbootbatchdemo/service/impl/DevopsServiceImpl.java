package com.neodain.springbootbatchdemo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.neodain.springbootbatchdemo.entity.Devops;
import com.neodain.springbootbatchdemo.entity.DevopsDto.DevopsRequest;
import com.neodain.springbootbatchdemo.entity.DevopsDto.DevopsResponse;
import com.neodain.springbootbatchdemo.repository.DevopsRepository;
import com.neodain.springbootbatchdemo.service.DevopsService;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsServiceImpl implements DevopsService {

    private final DevopsRepository repository;

    @Override
    public DevopsResponse create(DevopsRequest request) {
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
                .orElse(null);
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
                }).orElse(null);
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
