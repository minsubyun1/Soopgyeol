package com.soopgyeol.api.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String email;
    private String nickname;
}