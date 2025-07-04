package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.item.entity.Item;
import com.soopgyeol.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.soopgyeol.api.domain.buy.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsByUserAndItem(User user, Item item);
}
