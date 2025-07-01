package com.soopgyeol.api.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthLoginResponse {
    private String jwtToken;
    private boolean isNewUser;
}
