package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.usercarbonlog.entity.UserCarbonLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserCarbonLogRepository extends JpaRepository<UserCarbonLog, Long> {

    List <UserCarbonLog> findByUserIdAndRecordedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

}
