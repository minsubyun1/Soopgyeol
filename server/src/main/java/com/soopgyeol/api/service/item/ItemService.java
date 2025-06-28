package com.soopgyeol.api.service.item;

import com.soopgyeol.api.domain.enums.ItemCategory;
import com.soopgyeol.api.domain.item.dto.ItemResponse;
import java.util.List;

public interface ItemService {
  List<ItemResponse> getItemsByUserIdAndCategory(Long userId, ItemCategory category);

  List<ItemResponse> getDisplayedItemsByUserId(Long userId);
}