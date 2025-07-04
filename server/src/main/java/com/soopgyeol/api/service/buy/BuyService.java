package com.soopgyeol.api.service.buy;

import com.soopgyeol.api.domain.buy.dto.BuyResult;

public interface BuyService {
    BuyResult buyItem(Long userId, Long itemId);   // 반드시 BuyResult로 변경
}