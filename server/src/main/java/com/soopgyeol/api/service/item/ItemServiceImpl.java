package com.soopgyeol.api.service.item;

import com.soopgyeol.api.domain.enums.ItemCategory;
import com.soopgyeol.api.domain.item.dto.ItemResponse;
import com.soopgyeol.api.domain.item.entity.Inventory;
import com.soopgyeol.api.domain.item.entity.Item;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.InventoryRepository;
import com.soopgyeol.api.repository.ItemRepository;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final UserRepository userRepository;
  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;

  @Override
  public List<ItemResponse> getItemsByUserIdAndCategory(Long userId, ItemCategory category) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    List<Item> items = itemRepository.findByCategory(category);
    List<Inventory> inventories = inventoryRepository.findByUser(user);
    return items.stream().map(item -> {
      Inventory inventory = inventories.stream()
          .filter(inv -> inv.getItem().getId().equals(item.getId()))
          .findFirst()
          .orElse(null);
      boolean bought = inventory != null && inventory.isBuyed();
      boolean displayed = inventory != null && inventory.isDisplayed();
      boolean available = user.getMoneyBalance() >= item.getPrice();
      return ItemResponse.builder()
          .id(item.getId())
          .name(item.getName())
          .price(item.getPrice())
          .url(item.getUrl())
          .category(item.getCategory())
          .display(displayed)
          .available(available)
          .isBuyed(bought)
          .build();
    }).collect(Collectors.toList());
  }

  @Override
  public List<ItemResponse> getDisplayedItemsByUserId(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    return inventoryRepository.findByUserAndIsDisplayedTrue(user).stream()
        .map(inventory -> toDto(inventory, user.getMoneyBalance()))
        .collect(Collectors.toList());
  }

  private ItemResponse toDto(Inventory inventory, int userMoneyBalance) {
    Item item = inventory.getItem();
    boolean available = userMoneyBalance >= item.getPrice();
    return ItemResponse.builder()
        .id(item.getId())
        .name(item.getName())
        .price(item.getPrice())
        .url(item.getUrl())
        .category(item.getCategory())
        .display(inventory.isDisplayed())
        .available(available)
        .build();
  }
}
