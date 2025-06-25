package com.soopgyeol.api.controller;

import com.soopgyeol.api.dto.oauth.OAuthLoginRequest;
import com.soopgyeol.api.dto.oauth.OAuthLoginResponse;
import com.soopgyeol.api.service.auth.GoogleOauth;
import com.soopgyeol.api.service.auth.KakaoOauth;
import com.soopgyeol.api.service.auth.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
public class AuthController {

    private final OAuthService oAuthService;
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;

    @PostMapping("/login")
    public OAuthLoginResponse login(@RequestBody OAuthLoginRequest request) {
        return oAuthService.login(request);
    }

    @GetMapping("/oauth2/google/url")
    public ResponseEntity<Map<String, String>> getGoogleLoginUrl() {
        String url = googleOauth.getOauthRedirectURL();
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/oauth2/kakao/url")
    public ResponseEntity<Map<String, String>> getKakaoLoginUrl() {
        String url = kakaoOauth.getOauthRedirectURL();
        return ResponseEntity.ok(Map.of("url", url));
    }

    // 구글 로그인 성공 후 리디렉트 → code만 프론트로 전달
    @GetMapping("/oauth2/google/code-log")
    public void googleAutoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/oauth?code=" + code);
    }

    // 카카오 로그인 성공 후 리디렉트 → code만 프론트로 전달
    @GetMapping("/oauth2/kakao/code-log")
    public void kakaoAutoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/oauth?code=" + code);
    }
}
