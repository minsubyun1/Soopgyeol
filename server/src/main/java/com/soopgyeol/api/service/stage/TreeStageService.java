package com.soopgyeol.api.service.stage;

import com.soopgyeol.api.domain.stage.dto.TreeStageResponse;

public interface TreeStageService {
  void updateTreeStageByGrowth(Long userId);

  TreeStageResponse getTreeStageMessage(Long userId);
}
