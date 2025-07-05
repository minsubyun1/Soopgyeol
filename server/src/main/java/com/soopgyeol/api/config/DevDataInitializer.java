package com.soopgyeol.api.config;

import com.soopgyeol.api.domain.user.Role;
import com.soopgyeol.api.domain.user.SocialLoginType;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner{

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if(userRepository.count() == 0) {
            User testUser = User.builder()
                    .nickname("테스트 유저")
                    .email("test@example.com")
                    .password("1234")
                    .role(Role.USER)
                    .provider(SocialLoginType.GOOGLE)
                    .build();

            userRepository.save(testUser);

            System.out.println("테스트 유저가 자동 등록되었습니다. ID: " + testUser.getId());
        }
    }
}
