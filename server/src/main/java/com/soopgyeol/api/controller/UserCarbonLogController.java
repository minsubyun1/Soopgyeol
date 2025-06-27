package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogResponse;
import com.soopgyeol.api.service.carbonlog.UserCarbonLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<List<UserCarbonLogResponse>>> getLogsByDate(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<UserCarbonLogResponse> logs = userCarbonLogService.getLogsByUserIdAndDate(userId, date);
        return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", logs));
    }
}
