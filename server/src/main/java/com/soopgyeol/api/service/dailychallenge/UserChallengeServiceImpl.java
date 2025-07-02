package com.soopgyeol.api.service.dailychallenge;

import com.soopgyeol.api.domain.challenge.dto.AIChallengePromptResult;
import com.soopgyeol.api.domain.challenge.dto.ChallengeCompleteResponse;
import com.soopgyeol.api.domain.challenge.dto.ChallengeTodayResponse;
import com.soopgyeol.api.domain.challenge.entity.DailyChallenge;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.userChallenge.entity.UserChallenge;
import com.soopgyeol.api.repository.DailyChallengeRepository;
import com.soopgyeol.api.repository.UserChallengeRepository;
import com.soopgyeol.api.repository.UserRepository;
import com.soopgyeol.api.service.gpt.AIChallengePromptService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserChallengeServiceImpl implements UserChallengeService {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AIChallengePromptService aiChallengePromptService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChallengeTodayResponse getTodayChallengeForUser(Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        // 오늘의 챌린지 조회 or GPT로 생성
        DailyChallenge dailyChallenge = dailyChallengeRepository.findByIsActiveTrue()
                .orElseGet(() -> {
                    // 이전 챌린지 비활성화
                    dailyChallengeRepository.deactivateAll();

                    // GPT로 새로운 챌린지 생성
                    AIChallengePromptResult gptResponse = aiChallengePromptService.generateChallenge();
                    DailyChallenge newChallenge = DailyChallenge.builder()
                            .title(gptResponse.getTitle())
                            .goalCount(gptResponse.getGoalCount())
                            .rewardMoney(gptResponse.getRewardMoney())
                            .carbonKeyword(gptResponse.getCarbonKeyword())
                            .category(gptResponse.getCategory())
                            .isActive(true)
                            .build();
                    return dailyChallengeRepository.save(newChallenge);
                });

        // 유저 챌린지 참여 기록 조회 or 생성
        UserChallenge userChallenge = userChallengeRepository.findByUserAndDailyChallenge(user, dailyChallenge)
                .orElseGet(() -> {
                    UserChallenge newChallenge = UserChallenge.builder()
                            .user(user)
                            .dailyChallenge(dailyChallenge)
                            .progressCount(0)
                            .isCompleted(false)
                            .build();
                    return userChallengeRepository.save(newChallenge);
                });

        // DTO로 반환
        return ChallengeTodayResponse.builder()
                .challengeId(dailyChallenge.getId())
                .title(dailyChallenge.getTitle())
                .goalCount(dailyChallenge.getGoalCount())
                .rewardMoney(dailyChallenge.getRewardMoney())
                .carbonKeyword(dailyChallenge.getCarbonKeyword())
                .category(dailyChallenge.getCategory())
                .progressCount(userChallenge.getProgressCount())
                .isCompleted(userChallenge.isCompleted())
                .categoryImageUrl(dailyChallenge.getCategory().getImageUrl())
                .build();
    }

    @Override
    @Transactional
    public ChallengeCompleteResponse completeChallenge(Long userId, Long dailyChallengeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        DailyChallenge dailyChallenge = dailyChallengeRepository.findById(dailyChallengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지가 존재하지 않습니다."));

        UserChallenge userChallenge = userChallengeRepository.findByUserAndDailyChallenge(user, dailyChallenge)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 챌린지 기록이 없습니다."));

        if (!userChallenge.isCompleted()) {
            throw new IllegalArgumentException("아직 챌린지를 완료하지 않았습니다.");
        }

        if (userChallenge.isRewardReceived()) {
            throw new IllegalArgumentException("이미 보상을 받은 챌린지입니다.");
        }

        int reward = dailyChallenge.getRewardMoney();
        user.addMoney(reward);
        userChallenge.setRewardReceived(true);

        return new ChallengeCompleteResponse(reward, user.getMoneyBalance());
    }
}
