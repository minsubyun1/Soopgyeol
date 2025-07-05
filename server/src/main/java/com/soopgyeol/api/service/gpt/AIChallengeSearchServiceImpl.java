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

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIChallengeSearchServiceImpl implements AIChallengeSearchService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public CarbonAnalysisResponse analyzeWithFixedCategory(String keyword, Category fixedCategory) {
        String systemPrompt = """
                너는 탄소 분석 시스템이야. 사용자의 입력을 받고 다음 형식으로 JSON으로 대답해.
                품목명과, 탄소량 설명은 반드시 한글로 대답해줘.
                
                "growthPoint"는 사용자의 해당 활동 또는 소비에 따라서 탄소가 절감되는 정도를 너가 판단해서 0~20점 사이로 점수를 줘.
                ETC는 탄소량, growthPoint 모두 0점으로 줘.

                {
                  "name": (품목명, 한글),
                  "carbonGrams": (사용자의 입력에 따라 탄소 소비량을 분석해서 반환, 숫자, g 단위),
                  "growthPoint": (숫자, 정수 단위),
                  "explanation": (왜 이 탄소량이 나왔는지 설명. 30자 이내로 간결하게 작성)
                }
                """;

        List<Object> messages = List.of(
                buildMessage("system", systemPrompt),
                buildMessage("user", keyword)
        );

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

        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode json = objectMapper.readTree(content);

            return CarbonAnalysisResponse.builder()
                    .name(json.get("name").asText())
                    .carbonGrams(json.get("carbonGrams").asDouble())
                    .growthPoint(json.get("growthPoint").asInt())
                    .explanation(json.get("explanation").asText())
                    .category(fixedCategory) // GPT로부터 받지 않고 직접 삽입
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("챌린지 GPT 응답 파싱 실패", e);
        }
    }

    private Object buildMessage(String role, String content) {
        return new HashMap<>() {{
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
