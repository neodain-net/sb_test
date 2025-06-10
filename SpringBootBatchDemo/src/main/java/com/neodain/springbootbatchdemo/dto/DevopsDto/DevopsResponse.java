package com.neodain.springbootbatchdemo.dto.DevopsDto;

import java.time.LocalDateTime;

public record DevopsResponse(
    String devopsId,
    String name,
    String intro,
    LocalDateTime foundationTime) {}