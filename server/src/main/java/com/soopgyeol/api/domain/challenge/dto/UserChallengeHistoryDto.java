package com.soopgyeol.api.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class UserChallengeHistoryDto {
    private String title;
    private LocalDate createdAt;
    private boolean isCompleted;
}
