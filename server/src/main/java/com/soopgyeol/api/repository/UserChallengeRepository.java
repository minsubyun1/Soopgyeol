package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.challenge.entity.DailyChallenge;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.userChallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByUserAndDailyChallenge(User user, DailyChallenge dailyChallenge);
}
