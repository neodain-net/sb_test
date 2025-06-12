package com.neodain.springbootbatchdemo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.MemberDto.MemberRequest;
import com.neodain.springbootbatchdemo.dto.MemberDto.MemberResponse;
import com.neodain.springbootbatchdemo.service.IDevopsMemberService;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class DevopsMemberController {

    private final IDevopsMemberService service;

    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(service.create(request));
        // << GlobalExceptionHandler로 모든 예외를 처리 >>
        // try-catch 블록을 사용하여 따로, 개별적으로 예외를 처리할 수도 있지만,
        // 이 경우에는 GlobalExceptionHandler가 모든 예외를 처리할 수 있으므로
        // try-catch 블록을 사용하지 않아도 됩니다.
        // try { // If the member already exists, an AlreadyExistsException will be
        // thrown
        // return ResponseEntity.ok(service.create(request));
        // } catch (AlreadyExistsException e) {
        // return ResponseEntity.status(HttpStatus.CONFLICT).build();
        // }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> update(@PathVariable("id") String id,
            @RequestBody MemberRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}