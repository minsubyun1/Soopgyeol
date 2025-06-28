package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.domain.enums.ItemCategory;
import com.soopgyeol.api.domain.item.dto.ItemResponse;
import com.soopgyeol.api.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;

  @GetMapping("/items/user/{userId}/category/{category}")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getItemsByUserAndCategory(
      @PathVariable Long userId,
      @PathVariable ItemCategory category) {
    List<ItemResponse> items = itemService.getItemsByUserIdAndCategory(userId, category);
    return ResponseEntity.ok(new ApiResponse<>(true, "유저별 카테고리별 아이템 조회 성공", items));
  }

  @GetMapping("/items/user/{userId}/displayed")
  public ResponseEntity<ApiResponse<List<ItemResponse>>> getDisplayedItemsByUser(
      @PathVariable Long userId) {
    List<ItemResponse> items = itemService.getDisplayedItemsByUserId(userId);
    return ResponseEntity.ok(new ApiResponse<>(true, "유저의 전시 아이템 조회 성공", items));
  }
}
