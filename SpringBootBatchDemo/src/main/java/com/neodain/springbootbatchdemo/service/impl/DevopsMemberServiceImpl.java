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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
@Transactional
public class DevopsMemberServiceImpl implements IDevopsMemberService {

    private static final Logger logger = LoggerFactory.getLogger(DevopsMemberServiceImpl.class);

    private final IDevopsMemberRepository repository;

    @Override
    public MemberResponse create(MemberRequest request) {
        logger.info("Creating a new DevopsMember with name: {}", request.name());
        DevopsMember member = DevopsMember.builder()
                .memberId(UUID.randomUUID().toString())
                .name(request.name())
                .gender(DevopsMember.Gender.fromString(request.gender().toLowerCase()))
                .birthday(parseDate(request.birthday()))
                .phoneNum(request.phoneNum())
                .email(request.email())
                .build();
        repository.save(member);
        logger.info("DevopsMember created successfully with ID: {}", member.getMemberId());
        return toResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse get(String memberId) {
        logger.debug("Fetching DevopsMember with ID: {}", memberId);
        return repository.findById(memberId)
                .map(this::toResponse)
                // .orElse(null);
                .orElseGet(() -> {
                    logger.warn("DevopsMember with ID: {} not found", memberId);
                    return null;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> getAll() {
        logger.info("Fetching all DevopsMembers");
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse update(String memberId, MemberRequest request) {
        logger.info("Updating DevopsMember with ID: {}", memberId);
        return repository.findById(memberId)
                .map(entity -> {
                    entity.setName(request.name());
                    entity.setGender(DevopsMember.Gender.fromString(request.gender().toLowerCase()));
                    entity.setBirthday(parseDate(request.birthday()));
                    entity.setPhoneNum(request.phoneNum());
                    entity.setEmail(request.email());
                    logger.info("DevopsMember with ID: {} updated successfully", memberId);
                    return toResponse(repository.save(entity));
                // }).orElse(null);
                }).orElseGet(() -> {
                    logger.warn("DevopsMember with ID: {} not found for update", memberId);
                    return null;
                });
    }

    @Override
    public void delete(String memberId) {
        logger.info("Deleting DevopsMember with ID: {}", memberId);
        repository.deleteById(memberId);
        logger.info("DevopsMember with ID: {} deleted successfully", memberId);
    }

    private MemberResponse toResponse(DevopsMember member) {
        String birthStr = member.getBirthday() != null ? member.getBirthday().toString() : null;
        return new MemberResponse(
                member.getMemberId(),
                member.getName(),
                member.getGender().name(), // Enum → String 변환
                birthStr,
                member.getPhoneNum(),
                member.getEmail());
    }

    private Date parseDate(String value) {
        return (value == null || value.isEmpty()) ? null : Date.valueOf(value);
    }
}