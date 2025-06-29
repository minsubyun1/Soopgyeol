package com.soopgyeol.api.service.carbonlog;

import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserCarbonLogService {
    void saveCarbonLog(UserCarbonLogRequest request);

    List<UserCarbonLogResponse> getLogsByUserIdAndDate(Long userId, LocalDate date);
}
