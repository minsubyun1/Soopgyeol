package com.soopgyeol.api.dto;


import com.soopgyeol.api.domain.carbon.entity.CarbonItem;
import com.soopgyeol.api.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CarbonItemResponse {
    private final Long id;
    private final String name;
    private final Category category;
    private final float carbonValue;

    @Builder
    public CarbonItemResponse(Long id, String name, Category category, float carbonValue) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.carbonValue = carbonValue;
    }

    public static CarbonItemResponse from(CarbonItem item) {
        return CarbonItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .carbonValue(item.getCarbonValue())
                .build();
    }
}
