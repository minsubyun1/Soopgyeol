package com.soopgyeol.api.domain.usercarbonlog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCarbonLogResponse {
    private String product;
    private int growthPoint;
}
