package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogRequest;
import com.soopgyeol.api.domain.usercarbonlog.dto.UserCarbonLogResponse;
import com.soopgyeol.api.service.carbonlog.UserCarbonLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="탄소 소비 기록 API", description = "사용자 탄소 소비 기록 저장, 조회 등을 제공합니다.")
@RestController
@RequestMapping("/carbon/log")
@RequiredArgsConstructor
public class UserCarbonLogController {
    private final UserCarbonLogService userCarbonLogService;

    @Operation(summary = "탄소 소비 기록 저장", description = "사용자가 품목을 저장하면 소비 기록이 저장됩니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveLog(@RequestBody UserCarbonLogRequest request) {
        userCarbonLogService.saveCarbonLog(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "탄소 소비 기록 저장 완료", null));
    }

    @Operation(summary = "탄소 소비 기록 조회", description = "사용자의 모든 탄소 소비 기록을 조회합니다.")
    @GetMapping ResponseEntity<ApiResponse<List<UserCarbonLogResponse>>> getLogs(@RequestParam Long userId){
        List<UserCarbonLogResponse> logs = userCarbonLogService.getLogsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", logs));
    }


}
