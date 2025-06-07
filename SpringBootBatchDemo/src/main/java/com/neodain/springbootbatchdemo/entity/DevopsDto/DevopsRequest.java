package com.neodain.springbootbatchdemo.entity.DevopsDto;

import jakarta.validation.constraints.NotBlank;

public record DevopsRequest(
    @NotBlank String name,
    @NotBlank String intro) {
}
