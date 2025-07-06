package com.soopgyeol.api.domain.challenge.dto;

import com.soopgyeol.api.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class ChallengeTodayResponse {
    private Long challengeId;
    private String title;
    private int goalCount;
    private int rewardMoney;
    private String carbonKeyword;
    private Category category;
    private int progressCount;
    private boolean isCompleted;
    private String categoryImageUrl;
    private LocalDate createdAt;
}
