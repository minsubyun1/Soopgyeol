package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.enums.ItemCategory;
import com.soopgyeol.api.domain.item.dto.ItemResponse;
import com.soopgyeol.api.domain.item.dto.DisplayResponse;
import com.soopgyeol.api.service.item.ItemService;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @GetMapping("/items/category/{category}")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemsByUserAndCategory(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable ItemCategory category) {
    List<ItemResponse> items = itemService.getItemsByUserIdAndCategory(userDetails.getUserId(), category);
    return ResponseEntity.ok(new ApiResponse<>(true, "유저별 카테고리별 아이템 조회 성공", items));
  }

  @GetMapping("/items/displayed")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getDisplayedItemsByUser(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<ItemResponse> items = itemService.getDisplayedItemsByUserId(userDetails.getUserId());
    return ResponseEntity.ok(new ApiResponse<>(true, "유저의 전시 아이템 조회 성공", items));
  }

  @GetMapping("/items/inventory/category/{category}")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getBuyedItemsByUserAndCategory(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable ItemCategory category) {
    List<ItemResponse> items = itemService.getBuyedItemsByUserIdAndCategory(userDetails.getUserId(), category);
    return ResponseEntity.ok(new ApiResponse<>(true, "유저의 카테고리별 인벤토리(보유 아이템) 조회 성공", items));
  }

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
