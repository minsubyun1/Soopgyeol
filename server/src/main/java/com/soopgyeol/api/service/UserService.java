package com.soopgyeol.api.service;

import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        user.setNickname(nickname);
        userRepository.save(user);
        return nickname;
    }
}
