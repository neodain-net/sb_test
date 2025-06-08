package com.neodain.springbootbatchdemo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.dto.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.service.IDevopsMembershipService;

@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class DevopsMembershipController {

    private final IDevopsMembershipService service;

    @PostMapping
    public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipResponse> update(@PathVariable Long id,
                                                     @RequestBody MembershipRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}