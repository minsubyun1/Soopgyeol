package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.enums.ItemCategory;
import com.soopgyeol.api.domain.item.dto.ItemResponse;
import com.soopgyeol.api.domain.item.dto.DisplayResponse;
import com.soopgyeol.api.service.item.ItemService;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="아이템 API", description = "아이템과 관련된 모든 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @Operation(summary = "카테고리별 아이템 목록 조회",
          description = "특정 카테고리의 아이템 목록을 조회합니다. (상점 화면에서 사용 예상)")
  @GetMapping("/items/category/{category}")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemsByUserAndCategory(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable ItemCategory category) {
    List<ItemResponse> items = itemService.getItemsByUserIdAndCategory(userDetails.getUserId(), category);
    return ResponseEntity.ok(new ApiResponse<>(true, "유저별 카테고리별 아이템 조회 성공", items));
  }

  @Operation(summary = "유저별 전시 아이템 조회",
          description = "인증된 사용자가 전시 상태로 해둔 아이템 목록을 조회합니다. (홈 화면에서 사용 예상)")
  @GetMapping("/items/displayed")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getDisplayedItemsByUser(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<ItemResponse> items = itemService.getDisplayedItemsByUserId(userDetails.getUserId());
    return ResponseEntity.ok(new ApiResponse<>(true, "유저의 전시 아이템 조회 성공", items));
  }

  @Operation(summary = "유저별 인벤토리 카테고리별 조회",
          description = "인증된 사용자 아이템 카테고리 별 인벤토리(보유 아이템)를 조회합니다.")
  @GetMapping("/items/inventory/category/{category}")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getBuyedItemsByUserAndCategory(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable ItemCategory category) {
    List<ItemResponse> items = itemService.getBuyedItemsByUserIdAndCategory(userDetails.getUserId(), category);
    return ResponseEntity.ok(new ApiResponse<>(true, "유저의 카테고리별 인벤토리(보유 아이템) 조회 성공", items));
  }

  @Operation(summary = "아이템 전시 여부 수정",
          description = "인증된 사용자가 보유한 아이템의 전시 상태를 수정합니다.")
  @PatchMapping("/items/item/{itemId}/display")
  public ResponseEntity<ApiResponse<DisplayResponse>> toggleDisplay(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long itemId) {
    try {
      DisplayResponse response = itemService.toggleDisplay(userDetails.getUserId(), itemId);
      return ResponseEntity.ok(new ApiResponse<>(true, "전시 상태 변경 성공", response));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(409).body(new ApiResponse<>(false, e.getMessage(), null));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
    }
  }
}
