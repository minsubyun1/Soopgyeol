package com.soopgyeol.api.domain.challenge.entity;

import com.soopgyeol.api.domain.enums.Category;
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
public class DailyChallenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "daily_challenge_id")
  private Long id;

  private String title;

  @Column(name = "goal_count")
  private int goalCount;

  @Column(name = "reward_money")
  private int rewardMoney;

  @Column(name = "carbon_keyword")
  private String carbonKeyword;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "created_at")
  private LocalDate createdAt;

  @Enumerated(EnumType.STRING)
  private Category category;
}
