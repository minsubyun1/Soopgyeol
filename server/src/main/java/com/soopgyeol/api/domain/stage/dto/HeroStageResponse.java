package com.soopgyeol.api.domain.stage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroStageResponse {
  private String heroName;
  private String heroUrl;
  private String message;
}