package com.soopgyeol.api.controller;

import com.soopgyeol.api.domain.buy.dto.*;
import com.soopgyeol.api.service.jwt.JwtProvider;
import com.soopgyeol.api.service.buy.BuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items/buy")
@RequiredArgsConstructor
public class BuyController {

    private final BuyService buyService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<BuyResponse> buyItem(@RequestHeader("Authorization") String authorizationHeader,
                                          @RequestBody BuyRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtProvider.getUserId(token);

        BuyResult result = buyService.buyItem(userId, request.getItemId());

        BuyResponse response = BuyResponse.builder()
                .itemId(result.getItemId())
                .itemName(result.getItemName())
                .itemPrice(result.getItemPrice())
                .userMoneyBalance(result.getUserMoneyBalance())
                .message("구매가 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }
}