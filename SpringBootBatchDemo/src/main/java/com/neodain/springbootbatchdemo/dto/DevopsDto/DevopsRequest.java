package com.neodain.springbootbatchdemo.dto.DevopsDto;

import jakarta.validation.constraints.NotBlank;

public record DevopsRequest(
    @NotBlank String name,
    @NotBlank String intro) {}
