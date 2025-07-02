package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import com.soopgyeol.api.domain.challenge.dto.ChallengeCompleteRequest;
import com.soopgyeol.api.domain.challenge.dto.ChallengeCompleteResponse;
import com.soopgyeol.api.domain.challenge.dto.ChallengeTodayResponse;
import com.soopgyeol.api.service.dailychallenge.UserChallengeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class DailyChallengeController {
    private final UserChallengeServiceImpl userChallengeService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<ChallengeTodayResponse>> getOrCreateTodayChallenge (
           @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        ChallengeTodayResponse dto = userChallengeService.getTodayChallengeForUser(userDetails.getUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "오늘의 챌린지 조회 성공", dto));
    }

    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<ChallengeCompleteResponse>> completeChallenge(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChallengeCompleteRequest request){
        ChallengeCompleteResponse response = userChallengeService.completeChallenge(userDetails.getUserId(), request.getDailyChallengeId());
        return ResponseEntity.ok(new ApiResponse<>(true, "챌린지 포인트 지급 완료", response));
    }
}
