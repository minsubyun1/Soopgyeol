package com.soopgyeol.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginSuccessController {

    @GetMapping("/login-success")
    public String loginSuccess() {
        return "구글 로그인 성공했습니다.";
    }
}
