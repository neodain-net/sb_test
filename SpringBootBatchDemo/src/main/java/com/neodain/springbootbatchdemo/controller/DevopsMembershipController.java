package com.neodain.springbootbatchdemo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import com.neodain.springbootbatchdemo.entity.DevopsMembership.DevopsMembershipId;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipRequest;
import com.neodain.springbootbatchdemo.entity.MembershipDto.MembershipResponse;
import com.neodain.springbootbatchdemo.service.DevopsMembershipService;

@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class DevopsMembershipController {

    private final DevopsMembershipService service;

    @PostMapping
    public ResponseEntity<MembershipResponse> create(@RequestBody MembershipRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{devopsId}/{memberId}")
    public ResponseEntity<MembershipResponse> get(@PathVariable String devopsId,
                                                  @PathVariable String memberId) {
        return ResponseEntity.ok(service.get(new DevopsMembershipId(devopsId, memberId)));
    }

    @PutMapping("/{devopsId}/{memberId}")
    public ResponseEntity<MembershipResponse> update(@PathVariable String devopsId,
                                                     @PathVariable String memberId,
                                                     @RequestBody MembershipRequest request) {
        return ResponseEntity.ok(service.update(new DevopsMembershipId(devopsId, memberId), request));
    }

    @DeleteMapping("/{devopsId}/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable String devopsId,
                                       @PathVariable String memberId) {
        service.delete(new DevopsMembershipId(devopsId, memberId));
        return ResponseEntity.noContent().build();
    }
}
