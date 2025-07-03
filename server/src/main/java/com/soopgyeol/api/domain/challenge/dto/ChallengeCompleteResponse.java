package com.soopgyeol.api.domain.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChallengeCompleteResponse {
    private int reward;
    private int totalBalance;
}
