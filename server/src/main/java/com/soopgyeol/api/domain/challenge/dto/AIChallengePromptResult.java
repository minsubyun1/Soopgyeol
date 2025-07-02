package com.soopgyeol.api.domain.challenge.dto;

import com.soopgyeol.api.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AIChallengePromptResult {
    private String title;
    private int goalCount;
    private int rewardMoney;
    private String carbonKeyword;
    private Category category;
}

