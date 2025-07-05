package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarbonItemRepository extends JpaRepository<CarbonItem, Long> {
}
