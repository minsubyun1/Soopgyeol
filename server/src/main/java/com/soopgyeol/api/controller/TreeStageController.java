package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.stage.dto.TreeStageResponse;
import com.soopgyeol.api.service.stage.TreeStageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TreeStageController {

  private final TreeStageService treeStageService;

  @GetMapping("/tree-stage/{userId}")
  public ResponseEntity<ApiResponse<TreeStageResponse>> getTreeStageMessage(
      @PathVariable Long userId,
      Authentication authentication) {
    // 인증 정보 활용 가능: String email = authentication.getName(); 등
    try {
      TreeStageResponse response = treeStageService.getTreeStageMessage(userId);
      return ResponseEntity.ok(new ApiResponse<>(true, "단계 메시지 조회 성공", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
    }
  }
}
