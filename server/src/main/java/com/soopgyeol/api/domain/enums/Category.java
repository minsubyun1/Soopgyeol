package com.soopgyeol.api.domain.enums;

public enum Category {
    FOOD("음식", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/FOOD.png","https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_FOOD.png"),
    TRANSPORT("교통", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/TRANSPORTATION.png", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_TRANSPORTATION.png"),
    CLOTHING("의류", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CLOTHING.png", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_CLOTHING.png"),
    HOUSING_ENERGY("주거 및 에너지", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/HOUSING_ENERGY.png", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_HOUSING_ENERGY.png"),
    RECYCLE_WASTE("리사이클 & 폐기물", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/RECYCLE.png", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_RECYCLE.png"),
    LIFESTYLE_CONSUMPTION("라이프스타일 & 소비", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/LIFESTYLE.png", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_LIFESTYLE.png"),
    ETC("기타", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/ETC.png", "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/CHALLENGE_ETC.png");

    private final String description;
    private final String searchImageUrl;
    private final String challengeImageUrl;

    Category(String description, String searchImageUrl, String challengeImageUrl) {
        this.description = description;
        this.searchImageUrl = searchImageUrl;
        this.challengeImageUrl = challengeImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getSearchImageUrl() {
        return searchImageUrl;
    }

    public String getChallengeImageUrl() {
        return challengeImageUrl;
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