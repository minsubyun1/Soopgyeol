package com.soopgyeol.api.domain.enums;

public enum Category {
    FOOD("음식", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/FOOD.jpg"),
    TRANSPORT("교통", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/TRANSPORTATION.jpg"),
    CLOTHING("의류", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CLOTHING.jpg"),
    HOUSING_ENERGY("주거 및 에너지", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/HOUSING_ENERGY.jpg"),
    RECYCLE_WASTE("리사이클 & 폐기물", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/RECYCLE.jpg"),
    LIFESTYLE_CONSUMPTION("라이프스타일 & 소비", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/LIFESTYLE.jpg"),
    ETC("기타", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/ETC.JPG");

    private final String description;
    private final String imageUrl;

    Category(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
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