package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.item.entity.Inventory;
import com.soopgyeol.api.domain.item.entity.Item;
import com.soopgyeol.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findByUser(User user);

  List<Inventory> findByUserAndIsDisplayedTrue(User user);

  List<Inventory> findByUserAndIsBuyedTrue(User user);

  List<Inventory> findByUserAndItem(User user, Item item);

  void deleteByUser(User user);
}