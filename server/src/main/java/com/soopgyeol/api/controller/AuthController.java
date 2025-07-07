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

//임시 토큰 코드 사용 시 활성화
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.UserRepository;
import com.soopgyeol.api.service.jwt.JwtProvider;
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


    @GetMapping("/oauth2/google/code-log")
    public void googleAutoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 기존 login() 재활용
        OAuthLoginRequest loginRequest = OAuthLoginRequest.builder()
                .provider("GOOGLE")
                .code(code)
                .build();

        OAuthLoginResponse loginResponse = oAuthService.login(loginRequest);

        // JWT 꺼내기
        String jwtToken = loginResponse.getJwtToken();

        // 앱용 딥링크로 변경
        response.sendRedirect("soopgyeol://oauth-callback/google?token=" + jwtToken);

    }




    @GetMapping("/oauth2/kakao/code-log")
    public void kakaoAutoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        OAuthLoginRequest loginRequest = OAuthLoginRequest.builder()
                .provider("KAKAO")
                .code(code)
                .build();

        OAuthLoginResponse loginResponse = oAuthService.login(loginRequest);

        String jwtToken = loginResponse.getJwtToken();

        // 앱 용 딥링크로 변경
        response.sendRedirect("soopgyeol://oauth-callback/kakao?token=" + jwtToken);
    }




    // 임시 토큰 생성시 활성화
//    private final UserRepository userRepository;
//    private final JwtProvider jwtProvider;
//    @PostMapping("/dev-login")
//    public OAuthLoginResponse devLogin(@RequestParam("userId") Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
//
//        String jwt = jwtProvider.createToken(user.getId(), user.getRole());
//
//        return OAuthLoginResponse.builder()
//                .jwtToken(jwt)
//                .isNewUser(false)
//                .build();
//    }


}
