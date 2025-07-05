package com.soopgyeol.api.service.hero;

import com.soopgyeol.api.domain.stage.dto.HeroStageResponse;

public interface HeroStageService {
  void updateHeroStageByGrowth(Long userId);

  HeroStageResponse getHeroStageMessage(Long userId);
}