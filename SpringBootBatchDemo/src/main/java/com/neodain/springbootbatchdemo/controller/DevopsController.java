package com.neodain.springbootbatchdemo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import com.neodain.springbootbatchdemo.entity.DevopsDto.DevopsRequest;
import com.neodain.springbootbatchdemo.entity.DevopsDto.DevopsResponse;
import com.neodain.springbootbatchdemo.service.IDevopsService;

@RestController
@RequestMapping("/devops")
@RequiredArgsConstructor
public class DevopsController {

    private final IDevopsService service;

    @PostMapping
    public ResponseEntity<DevopsResponse> create(@RequestBody DevopsRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevopsResponse> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<DevopsResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DevopsResponse> update(@PathVariable("id") String id,
                                                 @RequestBody DevopsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}