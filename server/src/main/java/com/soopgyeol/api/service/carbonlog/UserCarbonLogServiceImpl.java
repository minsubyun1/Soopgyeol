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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCarbonLogServiceImpl implements UserCarbonLogService{
    private final UserRepository userRepository;
    private final CarbonItemRepository carbonItemRepository;
    private final UserCarbonLogRepository userCarbonLogRepository;

    @Override
    public void saveCarbonLog(UserCarbonLogRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        CarbonItem carbonItem = carbonItemRepository.findById(request.getCarbonItemId())
                .orElseThrow(() -> new IllegalArgumentException("탄소 품목이 존재하지 않습니다."));

        float totalCarbon = carbonItem.getCarbonValue() * request.getQuantity();

        UserCarbonLog log = UserCarbonLog.builder()
                .user(user)
                .carbonItem(carbonItem)
                .quantity(request.getQuantity())
                .calculatedCarbon(totalCarbon)
                .recordedAt(LocalDateTime.now())
                .build();

        userCarbonLogRepository.save(log);
    }

    @Override
    public List<UserCarbonLogResponse> getLogsByUserId(Long userId){
        List<UserCarbonLog> logs = userCarbonLogRepository.findByUserId(userId);

        return logs.stream().map(log -> UserCarbonLogResponse.builder()
                .product(log.getCarbonItem().getName())
                .quantity(log.getQuantity())
                .carbon(log.getCalculatedCarbon())
                .recordedAt(log.getRecordedAt())
                .build())
                .toList();
    }
}
