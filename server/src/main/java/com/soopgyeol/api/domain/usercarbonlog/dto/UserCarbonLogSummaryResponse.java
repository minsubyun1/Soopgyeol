package com.soopgyeol.api.domain.usercarbonlog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserCarbonLogSummaryResponse {
    private List<UserCarbonLogResponse> logs;
    private int totalGrowthPoint;
}

