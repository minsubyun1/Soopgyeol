package com.soopgyeol.api.controller;

import com.soopgyeol.api.common.dto.NicknameUpdateRequest;
import com.soopgyeol.api.common.dto.NicknameUpdateResponse;
import com.soopgyeol.api.service.UserService;
import com.soopgyeol.api.service.jwt.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
}

