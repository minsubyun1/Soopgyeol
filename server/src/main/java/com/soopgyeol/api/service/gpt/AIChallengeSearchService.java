package com.soopgyeol.api.service.gpt;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.enums.Category;

public interface AIChallengeSearchService {
    CarbonAnalysisResponse analyzeWithFixedCategory(String keyword, Category category);
}
