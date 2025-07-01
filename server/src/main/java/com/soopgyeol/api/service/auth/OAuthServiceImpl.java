package com.soopgyeol.api.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soopgyeol.api.domain.user.Role;
import com.soopgyeol.api.domain.user.SocialLoginType;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.dto.oauth.*;
import com.soopgyeol.api.repository.UserRepository;
import com.soopgyeol.api.service.jwt.JwtProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class OAuthServiceImpl implements OAuthService {


    private final Map<SocialLoginType, SocialOauth> oauthMap = new EnumMap<>(SocialLoginType.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Set<String> usedCodes = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public OAuthServiceImpl(List<SocialOauth> oauthList,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtProvider jwtProvider) {

        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider     = jwtProvider;

        oauthList.forEach(o -> oauthMap.put(o.type(), o));
    }

    @PostConstruct
    void logProvider() {
        System.out.println(">>> 활성 SocialOauth = " + oauthMap.keySet()); // [GOOGLE, KAKAO]
    }


    @Override
    public OAuthLoginResponse login(OAuthLoginRequest request) {



        if (!usedCodes.add(request.getCode())) {
            System.out.println("### DUPLICATE CODE BLOCKED: " + request.getCode());
            throw new IllegalStateException("이미 사용한 authorization_code");
        }

        scheduler.schedule(() -> usedCodes.remove(request.getCode()),
                10, TimeUnit.MINUTES);


        SocialLoginType type;
        try {
            type = SocialLoginType.valueOf(request.getProvider().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("지원하지 않는 provider: " + request.getProvider());
        }


        SocialOauth oauth = oauthMap.get(type);
        if (oauth == null)
            throw new IllegalStateException("provider 매핑 실패: " + type);


        String tokenJson = oauth.requestAccessToken(request.getCode());
//        System.out.println(">>> tokenJson = " + tokenJson);


        String accessToken;
        String idToken = null;
        try {
            if (type == SocialLoginType.GOOGLE) {
                GoogleTokenResponse res = mapper.readValue(tokenJson, GoogleTokenResponse.class);
                accessToken = res.getAccessToken();
                idToken     = res.getIdToken();
            } else { // kakao
                KakaoTokenResponse res = mapper.readValue(tokenJson, KakaoTokenResponse.class);
                accessToken = res.getAccessToken();
            }
        } catch (IOException e) {
            throw new IllegalStateException("토큰 JSON 파싱 실패", e);
        }


        SocialUserInfo info;
        try {
            info = oauth.getUserInfo(accessToken, idToken);
        } catch (IOException e) {
            throw new IllegalStateException("사용자 정보 조회 실패", e);
        }


        User user = userRepository
                .findBySocialIdAndProvider(info.getSocialId(), type)
                .orElseGet(() -> userRepository.saveAndFlush(
                        User.builder()
                                .email(info.getEmail())
                                .nickname(info.getNickname())
                                .provider(type)
                                .socialId(info.getSocialId())
                                .password(passwordEncoder.encode("dummy"))
                                .role(Role.USER)
                                .build()));


        String jwt = jwtProvider.createToken(user.getId(), user.getRole());


        return OAuthLoginResponse.builder()
                .jwtToken(jwt)
                .isNewUser(user.getCreatedAt().equals(user.getUpdatedAt()))
                .build();
    }

}
