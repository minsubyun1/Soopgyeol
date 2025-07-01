package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.stage.dto.TreeStageResponse;
import com.soopgyeol.api.service.stage.TreeStageService;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TreeStageController {

  private final TreeStageService treeStageService;

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
