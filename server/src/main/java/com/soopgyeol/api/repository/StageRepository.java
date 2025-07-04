package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.stage.entity.Stage;
import com.soopgyeol.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long> {
  Optional<Stage> findByUser(User user);

  void deleteByUser(User user);
}
