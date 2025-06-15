package com.soopgyeol.api.domain.carbon.dto;

import com.soopgyeol.api.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarbonAnalysisResponse {
    private String name;
    private double carbonGrams;
    private Category category;
    private String explanation;
}
