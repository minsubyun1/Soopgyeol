package com.soopgyeol.api.service.carbonlog;

import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogResponse;
import com.soopgyeol.api.domain.usercarbonlog.entity.UserCarbonLog;
import com.soopgyeol.api.repository.CarbonItemRepository;
import com.soopgyeol.api.repository.UserCarbonLogRepository;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCarbonLogServiceImpl implements UserCarbonLogService{
    private final UserRepository userRepository;
    private final CarbonItemRepository carbonItemRepository;
    private final UserCarbonLogRepository carbonLogRepository;

    @Override
    @Transactional
    public void saveCarbonLog(UserCarbonLogRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        CarbonItem carbonItem = carbonItemRepository.findById(request.getCarbonItemId())
                .orElseThrow(() -> new IllegalArgumentException("탄소 품목이 존재하지 않습니다."));

        float totalCarbon = carbonItem.getCarbonValue() * request.getQuantity();
        int totalGrowthPoint = carbonItem.getGrowthPoint() * request.getQuantity();

        // User 엔티티의 growthPoint 갱신
        user.setGrowthPoint(user.getGrowthPoint() + totalGrowthPoint);
        userRepository.save(user);

        UserCarbonLog log = UserCarbonLog.builder()
                .user(user)
                .carbonItem(carbonItem)
                .quantity(request.getQuantity())
                .calculatedCarbon(totalCarbon)
                .growthPoint(totalGrowthPoint)
                .recordedAt(LocalDateTime.now())
                .build();

        carbonLogRepository.save(log);
    }

    public List<UserCarbonLogResponse> getLogsByUserIdAndDate(Long userId, LocalDate date){
        // 날짜 기준으로 시작/끝 시간
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<UserCarbonLog> logs = carbonLogRepository.findByUserIdAndRecordedAtBetween(userId, startOfDay, endOfDay);

        return logs.stream()
                .map(log -> UserCarbonLogResponse.builder()
                        .product(log.getCarbonItem().getName())
                        .growthPoint(log.getGrowthPoint())
                        .build())
                .toList();
    }
}
