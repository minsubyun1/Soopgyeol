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
    private Long carbonItemId;
    private String name;             // 상품명
    private double carbonGrams;      // 탄소량 (g)
    private Category category;       // 카테고리
    private String categoryKorean;   // 카테고리(한글)
    private int growthPoint; // 단위당 성장 점수
    private String explanation;      // 왜 이 정도 탄소가 나왔는지 설명
}
