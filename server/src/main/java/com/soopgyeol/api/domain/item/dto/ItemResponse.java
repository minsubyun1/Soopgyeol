package com.soopgyeol.api.domain.item.dto;

import com.soopgyeol.api.domain.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
  private Long id;
  private String name;
  private int price;
  private String url;
  private ItemCategory category;
  private boolean display;
  private boolean available;
  private boolean isBuyed;
}
