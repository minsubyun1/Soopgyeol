package com.soopgyeol.api.service.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soopgyeol.api.domain.user.SocialLoginType;
import com.soopgyeol.api.dto.oauth.SocialUserInfo;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@Component
@AllArgsConstructor
public class GoogleOauth implements SocialOauth {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper om  = new ObjectMapper();

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    @Autowired
    public GoogleOauth(Dotenv dotenv) {

        this.clientId = dotenv.get("GOOGLE_CLIENT_ID");
        this.clientSecret = dotenv.get("GOOGLE_CLIENT_SECRET");
        this.redirectUri = dotenv.get("GOOGLE_REDIRECT_URI");


    }
    @Override
    public SocialLoginType type() {
        return SocialLoginType.GOOGLE;
    }

    @Override
    public String getOauthRedirectURL() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id",     clientId)
                .queryParam("redirect_uri",  redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope",         "openid email profile")
                .build()
                .encode()
                .toUriString();
    }

    @Override
    public String requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            String tokenJson = rest.postForObject(
                    "https://oauth2.googleapis.com/token",
                    request,
                    String.class
            );
            System.out.println("구글 토큰 응답: " + tokenJson);
            return tokenJson;
        } catch (HttpClientErrorException ex) {
            System.out.println("token 요청 실패: " + ex.getResponseBodyAsString());
            throw ex;
        }
    }


    @Override
    public SocialUserInfo getUserInfo(String accessToken, String ignore) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);

        String json = rest.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(h),
                String.class
        ).getBody();

        JsonNode n = om.readTree(json);
        return new SocialUserInfo(
                n.get("sub").asText(),
                n.get("email").asText(),
                n.get("name").asText()
        );
    }
}