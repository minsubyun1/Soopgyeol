package com.soopgyeol.api.service.carbon;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.domain.enums.Category;
import com.soopgyeol.api.repository.CarbonItemRepository;
import com.soopgyeol.api.service.gpt.AIChallengeSearchService;
import com.soopgyeol.api.service.gpt.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarbonAnalysisServiceImpl implements CarbonAnalysisService {
    private final OpenAiService openAiService;
    private final CarbonItemRepository carbonItemRepository;
    private final AIChallengeSearchService aiChallengeSearchService;

    @Override
    public CarbonAnalysisResponse analyzeAndSave(String userInput, Long userId) {
        // GPT 분석 요청
        CarbonAnalysisResponse analysis = openAiService.analyzeCarbon(userInput);

        analysis.setCategoryKorean(analysis.getCategory().getDescription());

        CarbonItem carbonItem = CarbonItem.builder()
                .name(analysis.getName())
                .category(analysis.getCategory())
                .carbonValue((float) analysis.getCarbonGrams())
                .growthPoint(analysis.getGrowthPoint())
                .explanation(analysis.getExplanation())
                .createdAt(LocalDateTime.now())
                .build();

        CarbonItem savedItem = carbonItemRepository.save(carbonItem);

        return CarbonAnalysisResponse.builder()
                .carbonItemId(savedItem.getId())
                .name(savedItem.getName())
                .category(savedItem.getCategory())
                .categoryKorean(analysis.getCategoryKorean())
                .carbonGrams(savedItem.getCarbonValue())
                .growthPoint(savedItem.getGrowthPoint())
                .explanation(savedItem.getExplanation())
                .categoryImageUrl(savedItem.getCategory().getImageUrl())
                .build();
    }

    @Override
    public CarbonAnalysisResponse analyzeByKeyword(String keyword, Category category) {
        // 1. GPT로 분석 요청 (챌린지에서 제공한 키워드 기반)
        CarbonAnalysisResponse analysis = aiChallengeSearchService.analyzeWithFixedCategory(keyword, category);


        analysis.setCategoryKorean(analysis.getCategory().getDescription());


        CarbonItem carbonItem = CarbonItem.builder()
                .name(analysis.getName())
                .category(analysis.getCategory())
                .carbonValue((float) analysis.getCarbonGrams())
                .growthPoint(analysis.getGrowthPoint())
                .explanation(analysis.getExplanation())
                .createdAt(LocalDateTime.now())
                .build();

        CarbonItem savedItem = carbonItemRepository.save(carbonItem);


        return CarbonAnalysisResponse.builder()
                .carbonItemId(savedItem.getId())  // 저장된 ID 포함
                .name(savedItem.getName())
                .category(savedItem.getCategory())
                .categoryKorean(analysis.getCategoryKorean())
                .carbonGrams(savedItem.getCarbonValue())
                .growthPoint(savedItem.getGrowthPoint())
                .explanation(savedItem.getExplanation())
                .categoryImageUrl(savedItem.getCategory().getImageUrl())
                .build();
    }
}
