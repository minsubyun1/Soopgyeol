package com.soopgyeol.api.config;

import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args){
        if(userRepository.count() == 0){
            User testUser = User.builder()
                    .name("테스트 유저")
                    .email("test@example.com")
                    .password("1234")
                    .moneyBalance(0)
                    .growthBalance(0)
                    .build();

            userRepository.save(testUser);

            System.out.println("테스트 유저가 자동 등록되었습니다. ID: " + testUser.getId());
        }
    }
}
