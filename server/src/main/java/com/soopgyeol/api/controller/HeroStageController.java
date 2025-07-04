package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.stage.dto.HeroStageResponse;
import com.soopgyeol.api.service.hero.HeroStageService;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="영웅 등급 API", description = "영웅등급과 관련된 모든 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
public class HeroStageController {

  private final HeroStageService heroStageService;

  @Operation(summary = "영웅 단계 조회",
          description = "인증된 사용자의 영웅(히어로) 성장 단계를 조회합니다.")
  @GetMapping("/hero-stage")
  public ResponseEntity<ApiResponse<HeroStageResponse>> getHeroStageMessage(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      HeroStageResponse response = heroStageService.getHeroStageMessage(userDetails.getUserId());
      return ResponseEntity.ok(new ApiResponse<>(true, "영웅 단계 메시지 조회 성공", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
    }
  }
}
