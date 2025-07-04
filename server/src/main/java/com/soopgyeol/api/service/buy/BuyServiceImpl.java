package com.soopgyeol.api.service.buy;

import com.soopgyeol.api.domain.buy.dto.BuyResult;
import com.soopgyeol.api.domain.buy.entity.Purchase;
import com.soopgyeol.api.domain.item.entity.Item;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.ItemRepository;
import com.soopgyeol.api.repository.PurchaseRepository;
import com.soopgyeol.api.repository.UserRepository;
import com.soopgyeol.api.common.exception.InsufficientBalanceException;
import com.soopgyeol.api.common.exception.ItemAlreadyOwnedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuyServiceImpl implements BuyService {

    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    public BuyServiceImpl(ItemRepository itemRepository,
                          PurchaseRepository purchaseRepository,
                          UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public BuyResult buyItem(Long userId, Long itemId) {

        // 1. 구매 가능 여부 (사용자 금액과 아이템 금액 비교)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템이 존재하지 않습니다."));

        if (user.getMoneyBalance() < item.getPrice()) {
            throw new InsufficientBalanceException("보유 금액이 부족합니다.");
        }

        // 2. 보유 중복 여부
        boolean alreadyOwned = purchaseRepository.existsByUserAndItem(user, item);
        if (alreadyOwned) {
            throw new ItemAlreadyOwnedException("이미 보유한 아이템입니다.");
        }

        // 3. 금액 차감
        int money = item.getPrice();
        user.subMoney(money);

        // 4. 인벤토리 저장
        Purchase purchase = Purchase.builder()
                .user(user)
                .item(item)
                .itemMoney(item.getPrice())
                .build();

        purchaseRepository.save(purchase);

        return BuyResult.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .itemPrice(item.getPrice())
                .userMoneyBalance(user.getMoneyBalance())
                .build();
    }
}