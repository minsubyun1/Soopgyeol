package com.soopgyeol.api.domain.carbon.entity;

import com.soopgyeol.api.domain.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carbon_item_id")
    private Long id;

    @Column(nullable = false)
    private String name; // 제품명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 카테고리

    @Column(name = "carbon_value", nullable = false)
    private float carbonValue; // 단위당 탄소량 (g 기준)

    @Column(name = "growth_point", nullable = false)
    private int growthPoint;

    @Column(columnDefinition = "TEXT")
    private String explanation; // GPT가 제공한 탄소량 설명

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
