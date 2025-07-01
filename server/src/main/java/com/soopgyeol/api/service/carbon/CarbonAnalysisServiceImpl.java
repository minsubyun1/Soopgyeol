package com.soopgyeol.api.service.carbon;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.repository.CarbonItemRepository;
import com.soopgyeol.api.service.gpt.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarbonAnalysisServiceImpl implements CarbonAnalysisService {
    private final OpenAiService openAiService;
    private final CarbonItemRepository carbonItemRepository;

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
                .build();
    }
}
