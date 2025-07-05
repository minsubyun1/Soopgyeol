package com.soopgyeol.api.service.carbonlog;

import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogResponse;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogSummaryResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserCarbonLogService {
    void saveCarbonLog(UserCarbonLogRequest request);

    UserCarbonLogSummaryResponse getLogsByUserIdAndDate(Long userId, LocalDate date);

    // 챌린지 로그만 조회
    UserCarbonLogSummaryResponse getChallengeLogsByUserIdAndDate(Long userId, LocalDate date);
}
