package com.soopgyeol.api.domain.item.entity;


import com.soopgyeol.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inventory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_item_id")
  private Long userItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  // 전시 여부
  @Column(name = "is_displayed")
  private boolean isDisplayed;

  // 보유(구매) 여부
  @Column(name = "is_buyed")
  private boolean isBuyed;

  // 구매 시간
  @Column(name = "buy_at")
  private LocalDateTime buyAt;
}
