package com.soopgyeol.api.service.gpt;

import com.soopgyeol.api.domain.challenge.dto.AIChallengePromptResult;

public interface AIChallengePromptService {
    AIChallengePromptResult generateChallenge();
}
