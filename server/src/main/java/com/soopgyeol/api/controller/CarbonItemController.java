package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisRequest;
import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.challenge.dto.AIChallengeSendingRequest;
import com.soopgyeol.api.service.carbon.CarbonAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soopgyeol.api.config.auth.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name="AI 탄소 검색 API", description = "AI를 통한 항목별 탄소 자동 검색을 제공합니다.")
@RestController
@RequestMapping("/carbon")
@RequiredArgsConstructor
public class CarbonItemController {

    private final CarbonAnalysisService carbonAnalysisService;

    @Operation(summary = "상품 검색", description = "상품명과 더불어 Chat GPT가 탄소량, 카테고리, 측정 이유 등을 함께 반환해줍니다.")
    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<CarbonAnalysisResponse>> analyzeCarbon(
            @RequestBody CarbonAnalysisRequest request) {
        CarbonAnalysisResponse response = carbonAnalysisService.analyzeAndSave(request.getUserInput());
        return ResponseEntity.ok(new ApiResponse<>(true, "검색 성공", response));
    }


    @Operation(summary = "챌린지 키워드 기반 탄소 분석", description = "챌린지 키워드를 기반으로 GPT가 탄소 분석 및 저장을 수행합니다.")
    @PostMapping("/analyze/keyword")
    public ResponseEntity<ApiResponse<CarbonAnalysisResponse>> analyzeByKeyword(@RequestBody AIChallengeSendingRequest request) {
        CarbonAnalysisResponse response = carbonAnalysisService.analyzeByKeyword(request.getKeyword(), request.getCategory(), request.getChallengeId()); // 저장 없이 반환
        return ResponseEntity.ok(new ApiResponse<>(true, "챌린지 기반 검색 성공", response));
    }
}
