package com.neodain.springbootbatchdemo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.neodain.springbootbatchdemo.dto.AddressDto.AddressRequest;
import com.neodain.springbootbatchdemo.dto.AddressDto.AddressResponse;
import com.neodain.springbootbatchdemo.service.IAddressService;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService service;

    @PostMapping("/member/{memberId}")
    public ResponseEntity<AddressResponse> create(@PathVariable String memberId,
            @RequestBody AddressRequest request) {
        return ResponseEntity.ok(service.create(memberId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,
            @RequestBody AddressRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
