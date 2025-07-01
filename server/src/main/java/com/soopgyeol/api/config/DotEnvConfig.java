package com.soopgyeol.api.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory(System.getProperty("user.dir"))  // 👈 명확하게 루트 경로 지정
                .ignoreIfMissing()
                .load();
    }
}