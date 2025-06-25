package com.soopgyeol.api.service.auth;

import com.soopgyeol.api.dto.oauth.OAuthLoginRequest;
import com.soopgyeol.api.dto.oauth.OAuthLoginResponse;


public interface OAuthService {
    OAuthLoginResponse login(OAuthLoginRequest request);
}
