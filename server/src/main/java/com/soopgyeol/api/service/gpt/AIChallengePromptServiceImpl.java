package com.soopgyeol.api.service.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soopgyeol.api.domain.challenge.dto.AIChallengePromptResult;
import com.soopgyeol.api.domain.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIChallengePromptServiceImpl implements AIChallengePromptService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public AIChallengePromptResult generateChallenge() {
        String systemPrompt = """
                너는 탄소 절감 챌린지를 추천해주는 시스템이야.
                오늘 사용자에게 줄 수 있는 탄소 절감 챌린지를 1개 추천해줘.
                
                - 응답은 JSON 형식으로만 해
                - challengeTitle은 15자 이내의 짧고 명확한 챌린지 제목
                - rewardMoney는 너가 판단하기에 챌린지 난이도에 따라 5 ~ 20으로 줘!
                - carbonKeyword는 기존 탄소 소비 분석 기능에서 사용하는 실생활 소비 키워드여야 해 (예: 다회용컵, 채식, 대중교통, 전기차, 에너지 절약, 음식물 쓰레기 줄이기 등)
                - category는 FOOD, TRANSPORT, CLOTHING, HOUSING_ENERGY, RECYCLE_WASTE, LIFESTYLE_CONSUMPTION 중 하나
                {
                  "challengeTitle": (챌린지명),
                  "category": (위 category 중 하나),
                  "goalCount": (목표횟수),
                  "rewardMoney": (보상 돈),
                  "carbonKeyword": (소비 키워드),
                }
                """;

        List<Object> messages = List.of(
                buildMessage("system", systemPrompt),
                buildMessage("user", "오늘의 챌린지를 생성해줘")

        );

        String requestBody = String.format("""
                {
                    "model": "gpt-3.5-turbo",
                    "messages": %s,
                    "temperature": 0.3
                }
                """, toJson(messages));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root
                    .path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText();

            JsonNode json = objectMapper.readTree(content);

            System.out.println("GPT 응답 content: " + content);

            return AIChallengePromptResult.builder()
                    .title(json.get("challengeTitle").asText())
                    .goalCount(json.get("goalCount").asInt())
                    .rewardMoney(json.get("rewardMoney").asInt())
                    .carbonKeyword(json.get("carbonKeyword").asText())
                    .category(Category.fromString(json.get("category").asText()))
                    .build();
        } catch (Exception e){
            throw new RuntimeException("GPT 챌린지 응답 파싱 실패", e);
        }
    }

    private Object buildMessage(String role, String content) {
        return new java.util.HashMap<>() {{
            put("role", role);
            put("content", content);
        }};
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }
}

