package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.stage.dto.TreeStageResponse;
import com.soopgyeol.api.service.stage.TreeStageService;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="나무 등급 API", description = "나무 등급과 관련된 모든 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
public class TreeStageController {

  private final TreeStageService treeStageService;

  @Operation(summary = "나무 단계 조회",
          description = "사용자의 현재 나무단계를 조회합니다.")
  @GetMapping("/tree-stage")
  public ResponseEntity<ApiResponse<TreeStageResponse>> getTreeStageMessage(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      TreeStageResponse response = treeStageService.getTreeStageMessage(userDetails.getUserId());
      return ResponseEntity.ok(new ApiResponse<>(true, "단계 메시지 조회 성공", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
    }
  }
}
