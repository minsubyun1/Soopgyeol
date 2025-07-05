package com.soopgyeol.api.domain.challenge.dto;

import lombok.Data;

@Data
public class ChallengeCompleteRequest {
    private Long userId;
    private Long dailyChallengeId;
}
