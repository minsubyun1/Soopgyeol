package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.challenge.entity.DailyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DailyChallengeRepository extends JpaRepository<DailyChallenge, Long> {
    Optional<DailyChallenge> findByIsActiveTrue();

    @Modifying
    @Query("UPDATE DailyChallenge dc SET dc.isActive = false WHERE dc.isActive = true")
    void deactivateAll();
}
