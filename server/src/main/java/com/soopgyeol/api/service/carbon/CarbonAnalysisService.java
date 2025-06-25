package com.soopgyeol.api.service.carbon;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;

public interface CarbonAnalysisService {
    CarbonAnalysisResponse analyzeAndSave(String userInput);
}
