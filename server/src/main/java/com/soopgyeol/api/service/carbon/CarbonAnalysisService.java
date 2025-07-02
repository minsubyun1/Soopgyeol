package com.soopgyeol.api.service.carbon;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;
import com.soopgyeol.api.domain.enums.Category;

public interface CarbonAnalysisService {
    CarbonAnalysisResponse analyzeAndSave(String userInput, Long userId);

    CarbonAnalysisResponse analyzeByKeyword(String keyword, Category category);
}
