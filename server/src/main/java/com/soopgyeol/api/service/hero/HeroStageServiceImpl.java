package com.soopgyeol.api.service.hero;

import com.soopgyeol.api.domain.stage.dto.HeroStageResponse;
import com.soopgyeol.api.domain.stage.entity.Stage;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.StageRepository;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeroStageServiceImpl implements HeroStageService {
  private final UserRepository userRepository; 
  private final StageRepository stageRepository;

  @Override
  @Transactional
  public void updateHeroStageByGrowth(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    Stage userStage = stageRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("사용자의 스테이지 정보가 없습니다."));

    int growth = user.getGrowthPoint();
    String heroName;
    String heroUrl;
    if (growth <= 100) { // 추후 사진 확정 시 수정 예정
      heroName = "견습 영웅";
      heroUrl = "https://example.com/hero-beginner.png";
    } else if (growth <= 300) {
      heroName = "신입 영웅";
      heroUrl = "https://example.com/hero-novice.png";
    } else if (growth <= 700) {
      heroName = "숙련 영웅";
      heroUrl = "https://example.com/hero-skilled.png";
    } else {
      heroName = "전설 영웅";
      heroUrl = "https://example.com/hero-legend.png";
    }

    userStage.setHeroName(heroName);
    userStage.setHeroUrl(heroUrl);
    stageRepository.save(userStage);
  }

  @Override
  @Transactional(readOnly = true)
  public HeroStageResponse getHeroStageMessage(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    Stage userStage = stageRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("사용자의 스테이지 정보가 없습니다"));

    String heroName = userStage.getHeroName();
    String heroUrl = userStage.getHeroUrl();
    if (heroName == null) {
      throw new IllegalArgumentException("사용자의 영웅 단계 정보가 없습니다");
    }

    String message;
    switch (heroName) {
      case "견습 영웅":
        message = "이제 막 모험을 시작한 견습 영웅입니다!";
        break;
      case "신입 영웅":
        message = "조금씩 성장하는 신입 영웅입니다!";
        break;
      case "숙련 영웅":
        message = "경험이 쌓인 숙련 영웅입니다!";
        break;
      case "전설 영웅":
        message = "전설이 된 영웅입니다! 최고예요!";
        break;
      default:
        message = heroName + " 단계입니다!";
    }

    return HeroStageResponse.builder()
        .heroName(heroName)
        .heroUrl(heroUrl)
        .message(message)
        .build();
  }
}
