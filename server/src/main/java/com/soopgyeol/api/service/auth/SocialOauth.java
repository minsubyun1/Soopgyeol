package com.soopgyeol.api.service.auth;

import com.soopgyeol.api.domain.user.SocialLoginType;
import com.soopgyeol.api.dto.oauth.SocialUserInfo;

import java.io.IOException;

public interface SocialOauth {
    SocialLoginType type();  // 구글 or 카카오
    String getOauthRedirectURL();
    String requestAccessToken(String code);
    SocialUserInfo getUserInfo(String accessToken, String idToken) throws IOException;}

