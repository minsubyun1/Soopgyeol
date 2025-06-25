package com.soopgyeol.api.common.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameUpdateRequest {
    @Size(min = 2, max = 12, message = "닉네임은 2자 이상 12자 이하로 입력해주세요.")
    @Pattern(regexp = ".*[a-zA-Z가-힣]+.*", message = "닉네임에는 한글 또는 영문자가 최소 1자 이상 포함되어야 합니다.")
    private String nickname;

}
