package com.soopgyeol.api.domain.userChallenge.entity;

import com.soopgyeol.api.domain.challenge.entity.DailyChallenge;
import com.soopgyeol.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChallenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_challenge_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "daily_challenge_id")
  private DailyChallenge dailyChallenge;

  @Column(name = "is_completed")
  private boolean isCompleted;

  @Column(name = "progress_count")
  private int progressCount;

  @Column(name = "reward_money")
  private int rewardMoney;

  @Column(name = "completed_at")
  private LocalDate completedAt;
}
