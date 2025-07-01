package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisRequest;
import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.service.carbon.CarbonAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/carbon")
@RequiredArgsConstructor
public class CarbonItemController {

    private final CarbonAnalysisService carbonAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<CarbonAnalysisResponse>> analyzeCarbon(
            @RequestBody CarbonAnalysisRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        CarbonAnalysisResponse response = carbonAnalysisService.analyzeAndSave(request.getUserInput(), userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "검색 성공", response));
    }
}
