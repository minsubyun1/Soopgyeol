package com.soopgyeol.api.domain.usercarbonlog.entity;

import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
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
public class UserCarbonLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carbon_item_id")
    private CarbonItem carbonItem;

    private int quantity;

    @Column(nullable = false)
    private float calculatedCarbon;

    @Column(nullable = false)
    private LocalDateTime recordedAt;
}
