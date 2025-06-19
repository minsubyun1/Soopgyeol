package com.soopgyeol.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "숲결 API 문서",
                description = "API 명세서",
                version = "v1"
        )
)
@SpringBootApplication
public class SoopgyeolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoopgyeolApiApplication.class, args);
    }

}