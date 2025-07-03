package com.soopgyeol.api.service.carbonlog;

import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.domain.challenge.entity.DailyChallenge;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.userChallenge.entity.UserChallenge;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogResponse;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogSummaryResponse;
import com.soopgyeol.api.domain.usercarbonlog.entity.UserCarbonLog;
import com.soopgyeol.api.repository.*;
import com.soopgyeol.api.service.stage.TreeStageService;
import com.soopgyeol.api.service.hero.HeroStageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;



@Service
@RequiredArgsConstructor
public class UserCarbonLogServiceImpl implements UserCarbonLogService {
        private final UserRepository userRepository;
        private final CarbonItemRepository carbonItemRepository;
        private final UserCarbonLogRepository carbonLogRepository;
        private final TreeStageService treeStageService;
        private final HeroStageService heroStageService;
        private final DailyChallengeRepository dailyChallengeRepository;
        private final UserChallengeRepository userChallengeRepository;

        @Override
        @Transactional
        public void saveCarbonLog(UserCarbonLogRequest request) {
                User user = userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

                CarbonItem carbonItem = carbonItemRepository.findById(request.getCarbonItemId())
                        .orElseThrow(() -> new IllegalArgumentException("탄소 품목이 존재하지 않습니다."));

                int quantity = request.getQuantity();
                float totalCarbon = carbonItem.getCarbonValue() * quantity;
                int totalGrowthPoint = carbonItem.getGrowthPoint() * quantity;

                // User 엔티티의 growthPoint 갱신
                user.setGrowthPoint(user.getGrowthPoint() + totalGrowthPoint);
                userRepository.save(user);

                // 단계 최신화 추가
                treeStageService.updateTreeStageByGrowth(user.getId());
                heroStageService.updateHeroStageByGrowth(user.getId());


                boolean isFromChallenge = false;
                if (request.getDailyChallengeId() != null){
                        isFromChallenge = true;

                        // 챌린지 정보 조회
                        DailyChallenge challenge = dailyChallengeRepository.findById(request.getDailyChallengeId())
                                .orElseThrow(() -> new IllegalArgumentException("챌린지 정보가 존재하지 않습니다."));

                        // 유저 챌린지 조회
                        UserChallenge userChallenge = userChallengeRepository.findByUserAndDailyChallenge(user, challenge)
                                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 챌린지 참여 정보가 없습니다."));

                        // 진행도 업데이트
                        userChallenge.increaseProgress(quantity);


                        userChallengeRepository.save(userChallenge);
                }


                UserCarbonLog log = UserCarbonLog.builder()
                        .user(user)
                        .carbonItem(carbonItem)
                        .quantity(request.getQuantity())
                        .calculatedCarbon(totalCarbon)
                        .growthPoint(totalGrowthPoint)
                        .recordedAt(LocalDateTime.now())
                        .isFromChallenge(isFromChallenge)
                        .build();

                carbonLogRepository.save(log);
        }

        public List<UserCarbonLogResponse> getLogsByUserIdAndDate(Long userId, LocalDate date) {
                // 날짜 기준으로 시작/끝 시간
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

                List<UserCarbonLog> logs = carbonLogRepository.findByUserIdAndRecordedAtBetween(userId, startOfDay,
                                endOfDay);

                return logs.stream()
                                .map(log -> UserCarbonLogResponse.builder()
                                                .product(log.getCarbonItem().getName())
                                                .growthPoint(log.getGrowthPoint())
                                                .build())
                                .toList();
        }

        public UserCarbonLogSummaryResponse getChallengeLogsByUserIdAndDate(Long userId, LocalDate date) {
                LocalDateTime start = date.atStartOfDay();
                LocalDateTime end = date.atTime(LocalTime.MAX);

                List<UserCarbonLog> logs = carbonLogRepository.findByUserIdAndRecordedAtBetweenAndIsFromChallengeTrue(
                        userId, start, end
                );

                int totalGrowthPoint = logs.stream()
                        .mapToInt(UserCarbonLog::getGrowthPoint)
                        .sum();

                List<UserCarbonLogResponse> logDtos = logs.stream()
                        .map(log -> UserCarbonLogResponse.builder()
                                .product(log.getCarbonItem().getName())
                                .growthPoint(log.getGrowthPoint())
                                .build())
                        .toList();

                return UserCarbonLogSummaryResponse.builder()
                        .logs(logDtos)
                        .totalGrowthPoint(totalGrowthPoint)
                        .build();
        }
}
