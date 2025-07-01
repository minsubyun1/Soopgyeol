package com.soopgyeol.api.domain.usercarbonlog.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class UserCarbonLogResponse {
    private String product;
    private int quantity;
    private float carbon;
    private LocalDateTime recordedAt;
    private int growthPoint;
}
