package com.soopgyeol.api.service.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public CarbonAnalysisResponse analyzeCarbon(String userInput) {
        String systemPrompt = """
                너는 탄소 분석 시스템이야. 사용자의 입력을 받고 다음 형식으로 JSON으로 대답해. "category"는 반드시 아래 목록 중 하나로만 대답해. 목록에 없는 경우에도 가장 유사한 주제로 골라야 해. 절대 ETC로 회피하지 마!
                카테고리 목록: Food, Transport, Clothing, Housing & Energy, Recycle & Waste, Lifestyle & Consumption, Etc
                "Etc"는 정말 해당하지 않는 경우에만 사용해. 가능하면 위의 6가지 중에서 골라.
                { "name": (제품명),
                  "carbonGrams": (숫자, g 단위),
                  "category": (반드시 위 목록 중 하나),
                  "explanation": (왜 이 탄소량이 나왔는지 설명. 30자 이내로 간결하게 작성)
                """;

        List<Object> messages = List.of(
                buildMessage("system", systemPrompt),
                buildMessage("user", userInput)
        );

        // 요청 바디 구성
        String requestBody = String.format("""
            {
              "model": "gpt-3.5-turbo",
              "messages": %s,
              "temperature": 0.2
            }
        """, toJson(messages));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // OpenAI API 호출
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

            String name = json.get("name").asText();
            double carbonGrams = json.get("carbonGrams").asDouble();
            String categoryStr = json.get("category").asText();
            String explanation = json.get("explanation").asText();

            return CarbonAnalysisResponse.builder()
                    .name(name)
                    .carbonGrams(carbonGrams)
                    .category(Category.fromString(categoryStr))
                    .explanation(explanation)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패", e);
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
