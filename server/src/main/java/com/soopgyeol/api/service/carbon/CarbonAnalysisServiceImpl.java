package com.soopgyeol.api.service.carbon;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.repository.CarbonItemRepository;
import com.soopgyeol.api.service.gpt.OpenAiService;
import com.soopgyeol.api.repository.UserRepository;
import com.soopgyeol.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.soopgyeol.api.service.stage.TreeStageService;
import com.soopgyeol.api.service.hero.HeroStageService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarbonAnalysisServiceImpl implements CarbonAnalysisService {
    private final OpenAiService openAiService;
    private final CarbonItemRepository carbonItemRepository;
    private final UserRepository userRepository;
    private final TreeStageService treeStageService;
    private final HeroStageService heroStageService;

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

        // 유저의 growthPoint 증가
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.increaseGrowthPoint(analysis.getGrowthPoint());
        userRepository.save(user);

        treeStageService.updateTreeStageByGrowth(userId);
        heroStageService.updateHeroStageByGrowth(userId);

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
