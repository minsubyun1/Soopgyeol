package com.soopgyeol.api.domain.buy.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuyResult {
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private int userMoneyBalance;
}