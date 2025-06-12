package com.neodain.springbootbatchdemo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.dto.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.exception.AlreadyExistsException;
import com.neodain.springbootbatchdemo.exception.NotFoundException;
import com.neodain.springbootbatchdemo.service.IDevopsMembershipService;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMembership;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMembership.Role;
import com.neodain.springbootbatchdemo.store.repository.IDevopsMemberRepository;
import com.neodain.springbootbatchdemo.store.repository.IDevopsMembershipRepository;
import com.neodain.springbootbatchdemo.store.repository.IDevopsRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsMembershipServiceImpl implements IDevopsMembershipService {

    private final IDevopsMembershipRepository repository;
    private final IDevopsRepository devopsRepository;
    private final IDevopsMemberRepository memberRepository;

    @Override
    public MembershipResponse create(MembershipRequest request) {
        var devops = devopsRepository.findById(request.devopsId())
                .orElseThrow(() -> new NotFoundException("Devops not found widh devopsId : " + request.devopsId()));
        // If devops is not found, throw NotFoundException
        var member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException("Devops not found with memberId : " + request.memberId()));
        // If member is not found, throw NotFoundException

        if (repository.existsByDevops_DevopsIdAndMember_MemberId(request.devopsId(), request.memberId())) {
            throw new AlreadyExistsException("Membership already exists for devopsId : " + request.devopsId() +
                    " and memberId : " + request.memberId());
        }

        DevopsMembership membership = DevopsMembership.builder()
                .devops(devops)
                .member(member)
                .role(Role.beginner)
                .joinDate(LocalDateTime.now())
                .build();
        repository.save(membership);
        return toResponse(membership);
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipResponse get(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Devops not found with ID : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipResponse update(Long id, MembershipRequest request) {
        return repository.findById(id)
                .map(entity -> {
                    var devops = devopsRepository.findById(request.devopsId()).orElse(null);
                    var member = memberRepository.findById(request.memberId()).orElse(null);
                    entity.setDevops(devops);
                    entity.setMember(member);
                    return toResponse(repository.save(entity));
                }).orElseThrow(() -> new NotFoundException("Devops not found with ID : " + id));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private MembershipResponse toResponse(DevopsMembership membership) {
        return new MembershipResponse(
                membership.getId(),
                membership.getMember().getMemberId(),
                membership.getDevops().getDevopsId(),
                membership.getRole().name(),
                membership.getJoinDate());
    }
}