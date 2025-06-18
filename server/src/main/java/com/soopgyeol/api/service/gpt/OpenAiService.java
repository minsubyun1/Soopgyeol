package com.soopgyeol.api.service.gpt;

import com.soopgyeol.api.domain.carbon.dto.CarbonAnalysisResponse;

public interface OpenAiService {
    CarbonAnalysisResponse analyzeCarbon(String userInput);
}
