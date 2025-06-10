package com.neodain.springbootbatchdemo.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.MemberDto.MemberRequest;
import com.neodain.springbootbatchdemo.dto.MemberDto.MemberResponse;
import com.neodain.springbootbatchdemo.service.IDevopsMemberService;
import com.neodain.springbootbatchdemo.store.jpo.DevopsMember;
import com.neodain.springbootbatchdemo.store.repository.IDevopsMemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsMemberServiceImpl implements IDevopsMemberService {

    private final IDevopsMemberRepository repository;

    @Override
    public MemberResponse create(MemberRequest request) {
        DevopsMember member = DevopsMember.builder()
                .memberId(UUID.randomUUID().toString())
                .name(request.name())
                .nickname(request.nickname())
                .gender(DevopsMember.Gender.fromString(request.gender().toLowerCase()))
                .birthday(parseDate(request.birthday()))
                .phoneNum(request.phoneNum())
                .email(request.email())
                .build();
        repository.save(member);
        return toResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse get(String memberId) {
        return repository.findById(memberId)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse update(String memberId, MemberRequest request) {
        return repository.findById(memberId)
                .map(entity -> {
                    entity.setName(request.name());
                    entity.setNickname(request.nickname());
                    entity.setGender(DevopsMember.Gender.fromString(request.gender().toLowerCase()));
                    entity.setBirthday(parseDate(request.birthday()));
                    entity.setPhoneNum(request.phoneNum());
                    entity.setEmail(request.email());
                    return toResponse(repository.save(entity));
                }).orElse(null);
    }

    @Override
    public void delete(String memberId) {
        repository.deleteById(memberId);
    }

    private MemberResponse toResponse(DevopsMember member) {
        String birthStr = member.getBirthday() != null ? member.getBirthday().toString() : null;
        return new MemberResponse(
                member.getMemberId(),
                member.getName(),
                member.getNickname(),
                member.getGender().name(), // Enum → String 변환
                birthStr,
                member.getPhoneNum(),
                member.getEmail());
    }

    private Date parseDate(String value) {
        return (value == null || value.isEmpty()) ? null : Date.valueOf(value);
    }
}