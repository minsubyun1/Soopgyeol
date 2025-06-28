package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.item.entity.Item;
import com.soopgyeol.api.domain.enums.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
  List<Item> findByCategory(ItemCategory category);
}
