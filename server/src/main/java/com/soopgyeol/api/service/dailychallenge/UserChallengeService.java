package com.soopgyeol.api.service.dailychallenge;

import com.soopgyeol.api.domain.challenge.dto.ChallengeCompleteResponse;
import com.soopgyeol.api.domain.challenge.dto.ChallengeTodayResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeService {
    ChallengeTodayResponse getTodayChallengeForUser(Long userId);
    ChallengeCompleteResponse completeChallenge(Long userId, Long dailyChallengeId);
}
