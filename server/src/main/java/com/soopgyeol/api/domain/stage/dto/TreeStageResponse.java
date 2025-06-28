package com.soopgyeol.api.domain.stage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeStageResponse {
  private String treeName;
  private String treeUrl;
  private String message;
}
