package com.neodain.springbootbatchdemo.entity.DevopsDto;

import java.time.LocalDateTime;

public record DevopsResponse(
    String devopsId,
    String name,
    String intro,
    LocalDateTime foundationTime) {
}