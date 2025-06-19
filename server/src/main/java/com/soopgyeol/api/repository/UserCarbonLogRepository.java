package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.usercarbonlog.entity.UserCarbonLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCarbonLogRepository extends JpaRepository<UserCarbonLog, Long> {
    List<UserCarbonLog> findByUserId(Long userId);
}
