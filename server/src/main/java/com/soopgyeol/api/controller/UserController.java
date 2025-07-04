package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.NicknameUpdateRequest;
import com.soopgyeol.api.common.dto.NicknameUpdateResponse;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.user.dto.UserInfoResponse;
import com.soopgyeol.api.service.UserService;
import com.soopgyeol.api.service.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "사용자 정보 관리 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(
            summary = "닉네임 변경",
            description = "JWT 토큰으로 인증된 사용자의 닉네임을 변경합니다."
    )
    @PatchMapping("/me/nickname")
    public ResponseEntity<NicknameUpdateResponse> updateNickname(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody NicknameUpdateRequest request) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtProvider.getUserId(token);

        String updatedNickname = userService.updateNickname(userId, request.getNickname());
        return ResponseEntity.ok(new NicknameUpdateResponse(updatedNickname));
    }

    @Operation(
            summary = "사용자 정보 조회",
            description = "사용자의 기본 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtProvider.getUserId(token);

        User user = userService.getUserById(userId);

        UserInfoResponse response = new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "사용자의 유저 정보를 삭제하여 탈퇴 처리합니다."
    )
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtProvider.getUserId(token);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(
                    java.util.Map.of(
                            "success", true,
                            "message", "회원 탈퇴 성공",
                            "data", userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                    java.util.Map.of(
                            "success", false,
                            "message", e.getMessage(),
                            "data", null));
        }
    }

}