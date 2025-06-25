package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.service.carbonlog.UserCarbonLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carbon/log")
@RequiredArgsConstructor
public class UserCarbonLogController {
    private final UserCarbonLogService userCarbonLogService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveLog(@RequestBody UserCarbonLogRequest request) {
        userCarbonLogService.saveCarbonLog(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "탄소 소비 기록 저장 완료", null));
    }

}
