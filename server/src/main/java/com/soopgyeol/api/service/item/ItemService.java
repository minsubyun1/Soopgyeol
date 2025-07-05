package com.soopgyeol.api.service.item;

import com.soopgyeol.api.domain.enums.ItemCategory;
import com.soopgyeol.api.domain.item.dto.ItemResponse;
import com.soopgyeol.api.domain.item.dto.DisplayResponse;
import java.util.List;

public interface ItemService {
  List<ItemResponse> getItemsByUserIdAndCategory(Long userId, ItemCategory category);

  List<ItemResponse> getDisplayedItemsByUserId(Long userId);

  List<ItemResponse> getBuyedItemsByUserId(Long userId);

  List<ItemResponse> getBuyedItemsByUserIdAndCategory(Long userId, ItemCategory category);

  DisplayResponse toggleDisplay(Long userId, Long itemId);
}