package com.soopgyeol.api.domain.buy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class BuyResponse {
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private int userMoneyBalance;
    private String message;
}
