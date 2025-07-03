package com.soopgyeol.api.domain.usercarbonlog.entity;

import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.domain.challenge.entity.DailyChallenge;
import com.soopgyeol.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user_carbon_log")
public class UserCarbonLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    // 소비한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 소비한 품목
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carbon_item_id")
    private CarbonItem carbonItem;

    // 소비 수량
    private int quantity;

    // 계산된 총 탄소량
    @Column(name = "calculated_carbon", nullable = false)
    private float calculatedCarbon;

    @Column(name = "calculated_point", nullable = false)
    private int growthPoint;

    // 저장 시각
    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @Column(nullable = false)
    private boolean isFromChallenge;
}
