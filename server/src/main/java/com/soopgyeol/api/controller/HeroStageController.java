package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.stage.dto.HeroStageResponse;
import com.soopgyeol.api.service.hero.HeroStageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeroStageController {

  private final HeroStageService heroStageService;

  @GetMapping("/hero-stage/{userId}")
  public ResponseEntity<ApiResponse<HeroStageResponse>> getHeroStageMessage(
      @PathVariable Long userId,
      Authentication authentication) {
    // 인증 정보 활용 가능: String email = authentication.getName(); 등
    try {
      HeroStageResponse response = heroStageService.getHeroStageMessage(userId);
      return ResponseEntity.ok(new ApiResponse<>(true, "영웅 단계 메시지 조회 성공", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
    }
  }
}
