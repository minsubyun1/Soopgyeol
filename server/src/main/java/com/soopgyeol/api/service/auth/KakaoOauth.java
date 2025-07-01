package com.soopgyeol.api.service.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soopgyeol.api.domain.user.SocialLoginType;
import com.soopgyeol.api.dto.oauth.SocialUserInfo;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper om = new ObjectMapper();
    private final Dotenv dotenv = Dotenv.load();

    private final String clientId     = dotenv.get("KAKAO_CLIENT_ID");

    //private final String redirectUri  = dotenv.get("KAKAO_REDIRECT_URI");
    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;
    private final String clientSecret = dotenv.get("KAKAO_CLIENT_SECRET"); // optional

    @Override
    public SocialLoginType type() {
        return SocialLoginType.KAKAO;
    }

    @Override
    public String getOauthRedirectURL() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "profile_nickname account_email")
                .build()
                .toUriString();
    }

    @Override
    public String requestAccessToken(String code) {
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type",    "authorization_code");
        body.add("client_id",     clientId);
        body.add("client_secret", clientSecret);
        body.add("code",          code);
        body.add("redirect_uri",  redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return rest.postForObject(
                "https://kauth.kakao.com/oauth/token",
                new HttpEntity<>(body, headers),
                String.class
        );
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken, String ignore) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        String json = rest.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        ).getBody();

        JsonNode node = om.readTree(json);
        return new SocialUserInfo(
                String.valueOf(node.get("id").asLong()),
                node.at("/kakao_account/email").asText(),
                node.at("/kakao_account/profile/nickname").asText()
        );
    }
}