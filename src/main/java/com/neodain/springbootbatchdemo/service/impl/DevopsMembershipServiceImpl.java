package com.neodain.springbootbatchdemo.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.neodain.springbootbatchdemo.entity.DevopsMembership;
import com.neodain.springbootbatchdemo.entity.DevopsMembership.Role;
import com.neodain.springbootbatchdemo.entity.DevopsMembership.DevopsMembershipId;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.repository.IDevopsMembershipRepository;
import com.neodain.springbootbatchdemo.service.IDevopsMembershipService;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsMembershipServiceImpl implements IDevopsMembershipService {

    private final IDevopsMembershipRepository repository;

    @Override
    public MembershipResponse create(MembershipRequest request) {
        DevopsMembership membership = DevopsMembership.builder()
                .devopsId(request.devopsId())
                .memberId(request.memberId())
                .role(Role.beginner)
                .joinDate(LocalDate.now())
                .build();
        repository.save(membership);
        return toResponse(membership);
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipResponse get(DevopsMembershipId id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipResponse update(DevopsMembershipId id, MembershipRequest request) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setDevopsId(request.devopsId());
                    entity.setMemberId(request.memberId());
                    return toResponse(repository.save(entity));
                }).orElse(null);
    }

    @Override
    public void delete(DevopsMembershipId id) {
        repository.deleteById(id);
    }

    private MembershipResponse toResponse(DevopsMembership membership) {
        return new MembershipResponse(
                membership.getMemberId(),
                null,
                null,
                null,
                membership.getRole(),
                membership.getJoinDate().atStartOfDay());
    }
}