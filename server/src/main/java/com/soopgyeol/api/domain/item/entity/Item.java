package com.soopgyeol.api.domain.item.entity;


import com.soopgyeol.api.domain.enums.ItemCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int price;

  private String url;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ItemCategory category;
}
