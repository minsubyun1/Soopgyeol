package com.soopgyeol.api.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN

    @Enumerated(EnumType.STRING)
    private SocialLoginType provider; // GOOGLE, KAKAO

    private String socialId;

    @Column(name = "money_balance", nullable = false)
    private int moneyBalance;

    @Column(name = "growth_point", nullable = false)
    private int growthPoint;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void increaseGrowthPoint(int point) {
        this.growthPoint += point;
    }

    public void addMoney(int amount) {
        this.moneyBalance += amount;
    }
}