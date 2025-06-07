package com.neodain.springbootbatchdemo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.neodain.springbootbatchdemo.entity.DevopsMembership;
import com.neodain.springbootbatchdemo.entity.DevopsMembership.Role;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.repository.IDevopsMembershipRepository;
import com.neodain.springbootbatchdemo.repository.IDevopsMemberRepository;
import com.neodain.springbootbatchdemo.repository.IDevopsRepository;
import com.neodain.springbootbatchdemo.service.IDevopsMembershipService;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsMembershipServiceImpl implements IDevopsMembershipService {

    private final IDevopsMembershipRepository repository;
    private final IDevopsRepository devopsRepository;
    private final IDevopsMemberRepository memberRepository;

    @Override
    public MembershipResponse create(MembershipRequest request) {
        var devops = devopsRepository.findById(request.devopsId()).orElse(null);
        var member = memberRepository.findById(request.memberId()).orElse(null);
        if (devops == null || member == null) {
            return null;
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
    public MembershipResponse update(Long id, MembershipRequest request) {
        return repository.findById(id)
                .map(entity -> {
                    var devops = devopsRepository.findById(request.devopsId()).orElse(null);
                    var member = memberRepository.findById(request.memberId()).orElse(null);
                    entity.setDevops(devops);
                    entity.setMember(member);
                    return toResponse(repository.save(entity));
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private MembershipResponse toResponse(DevopsMembership membership) {
        return new MembershipResponse(
                membership.getId(),
                membership.getMember().getMemberId(),
                membership.getMember().getName(),
                membership.getMember().getEmail(),
                membership.getMember().getPhoneNum(),
                membership.getRole(),
                membership.getJoinDate());
    }
}