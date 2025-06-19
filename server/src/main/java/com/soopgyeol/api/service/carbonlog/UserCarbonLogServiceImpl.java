package com.soopgyeol.api.service.carbonlog;

import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.entity.UserCarbonLog;
import com.soopgyeol.api.repository.CarbonItemRepository;
import com.soopgyeol.api.repository.UserCarbonLogRepository;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCarbonLogServiceImpl implements UserCarbonLogService{
    private final UserRepository userRepository;
    private final CarbonItemRepository carbonItemRepository;
    private final UserCarbonLogRepository carbonLogRepository;

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
                .recordedAt(LocalDate.now())
                .build();

        carbonLogRepository.save(log);
    }
}
