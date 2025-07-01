package com.soopgyeol.api.service.scheduler;

import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGrowthScheduler {
  private final UserRepository userRepository;

  // 매일 자정(00:00)에 실행
  @Scheduled(cron = "0 0 0 * * *")
  public void increaseAllUsersGrowthPoint() {
    List<User> users = userRepository.findAll();
    for (User user : users) {
      user.increaseGrowthPoint(10);
    }
    userRepository.saveAll(users);
    log.info("모든 유저의 성장점수를 10점 증가시켰습니다.");
  }
}
