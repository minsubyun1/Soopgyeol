package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.item.entity.Inventory;
import com.soopgyeol.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findByUser(User user);

  List<Inventory> findByUserAndIsDisplayedTrue(User user);
}