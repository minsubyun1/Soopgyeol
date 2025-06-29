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
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/oauth")
public class AuthController {

    @Value("${oauth.redirect.frontend-url}")
    private String frontendRedirectUrl;

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

    @GetMapping("/oauth2/google/code-log")
    public void googleAutoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        response.sendRedirect(frontendRedirectUrl + "?code=" + code);
    }

    @GetMapping("/oauth2/kakao/code-log")
    public void kakaoAutoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        response.sendRedirect(frontendRedirectUrl + "?code=" + code);
    }
}
