package com.soopgyeol.api.domain.challenge.dto;

import com.soopgyeol.api.domain.enums.Category;
import lombok.Data;

@Data
public class AIChallengeSendingRequest {
    String keyword;
    Category category;
    private Long challengeId;
}
