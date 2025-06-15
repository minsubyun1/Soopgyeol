package com.soopgyeol.api.domain.enums;

public enum Category {
    FOOD("음식"),
    TRANSPORT("교통"),
    CLOTHING("의류"),
    HOUSING_ENERGY("주거 및 에너지"),
    RECYCLE_WASTE("리사이클 & 폐기물"),
    LIFESTYLE_CONSUMPTION("라이프스타일 & 소비"),
    ETC("기타");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Category fromString(String value) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(value.trim())) {
                return category;
            }
        }
        return ETC; // fallback
    }
}
