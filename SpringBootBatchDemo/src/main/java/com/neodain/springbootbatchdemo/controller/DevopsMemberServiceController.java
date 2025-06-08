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
public class DevopsMemberServiceController {

    private final IDevopsMemberService service;

    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(service.create(request));
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