package com.soopgyeol.api.service.carbonlog;

import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;

public interface UserCarbonLogService {
    void saveCarbonLog(UserCarbonLogRequest request);
}
