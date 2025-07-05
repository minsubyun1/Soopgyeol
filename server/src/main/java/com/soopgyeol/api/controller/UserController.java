package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.NicknameUpdateRequest;
import com.soopgyeol.api.common.dto.NicknameUpdateResponse;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.user.dto.UserInfoResponse;
import com.soopgyeol.api.service.UserService;
import com.soopgyeol.api.service.jwt.JwtProvider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PatchMapping("/me/nickname")
    public ResponseEntity<NicknameUpdateResponse> updateNickname(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody NicknameUpdateRequest request) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtProvider.getUserId(token);

        String updatedNickname = userService.updateNickname(userId, request.getNickname());
        return ResponseEntity.ok(new NicknameUpdateResponse(updatedNickname));
    }

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