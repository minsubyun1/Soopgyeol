package com.soopgyeol.api.domain.usercarbonlog.dto;

import lombok.Data;

@Data
public class UserCarbonLogRequest {
    private Long userId;
    private Long carbonItemId;
    private int quantity;
}
