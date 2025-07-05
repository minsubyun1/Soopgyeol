package com.soopgyeol.api.domain.buy.entity;

import com.soopgyeol.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import com.soopgyeol.api.domain.item.entity.Item;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buy_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 구매자 (FK)

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 구매한 아이템 (FK)

    @Column(name = "item_money", nullable = false)
    private int itemMoney;

    @CreationTimestamp
    @Column(name = "purchased_at", nullable = false, updatable = false)
    private LocalDateTime purchasedAt;  // 구매 시각
}
