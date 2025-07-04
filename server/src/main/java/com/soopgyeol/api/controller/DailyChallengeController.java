package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.ApiResponse;
import com.soopgyeol.api.config.auth.CustomUserDetails;
import com.soopgyeol.api.domain.challenge.dto.ChallengeCompleteRequest;
import com.soopgyeol.api.domain.challenge.dto.ChallengeCompleteResponse;
import com.soopgyeol.api.domain.challenge.dto.ChallengeTodayResponse;
import com.soopgyeol.api.domain.challenge.dto.UserChallengeHistoryDto;
import com.soopgyeol.api.service.dailychallenge.UserChallengeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Daily 챌린지 API", description = "일일챌린지와 관련된 모든 기능을 제공합니다.")
@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class DailyChallengeController {
    private final UserChallengeServiceImpl userChallengeService;

    @Operation(summary = "오늘의 챌린지 조회 (자동 생성 포함)",
            description = "사용자가 오늘의 챌린지를 처음 요청할 경우 GPT를 통해 자동 생성되고, 이후 동일한 챌린지를 반환합니다.")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<ChallengeTodayResponse>> getOrCreateTodayChallenge (
           @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        ChallengeTodayResponse dto = userChallengeService.getTodayChallengeForUser(userDetails.getUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "오늘의 챌린지 조회 성공", dto));
    }

    @Operation(summary = "챌린지 완료 처리",
            description = "챌린지 완수 여부를 확인하여 보상 지급")
    @PostMapping("/complete")
    public ResponseEntity<ApiResponse<ChallengeCompleteResponse>> completeChallenge(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChallengeCompleteRequest request){
        ChallengeCompleteResponse response = userChallengeService.completeChallenge(userDetails.getUserId(), request.getDailyChallengeId());
        return ResponseEntity.ok(new ApiResponse<>(true, "챌린지 포인트 지급 완료", response));
    }

    @Operation(summary = "챌린지 전체 조회",
            description = "사용자 챌린지 내역에 있는 수행 중 또는 완료된 챌린지 전체 조회")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<UserChallengeHistoryDto>>> getChallengeHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<UserChallengeHistoryDto> history = userChallengeService.getUserChallengeHistory(userDetails.getUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "챌린지 수행 이력 조회 성공", history));
    }


}
