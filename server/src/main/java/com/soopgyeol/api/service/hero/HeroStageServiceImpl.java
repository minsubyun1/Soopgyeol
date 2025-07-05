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
        .orElseGet(() -> stageRepository.save(Stage.builder()
            .user(user)
            .treeName("씨앗")
            .treeUrl("https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/seed.png")
            .heroName("Lv.1 새싹지기")
            .heroUrl("https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/hero/heroseed.png")
            .build()));

    int growth = user.getGrowthPoint();
    String heroName;
    String heroUrl;
    if (growth <= 100) {
      heroName = "Lv.1 새싹지기";
      heroUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/hero/heroseed.png";
    } else if (growth <= 300) {
      heroName = "Lv.2 줄임꾼";
      heroUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/hero/herosappling.png";
    } else if (growth <= 700) {
      heroName = "Lv.3 탐험가";
      heroUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/hero/herolittletree.png";
    } else {
      heroName = "Lv.4 지구지킴이";
      heroUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/hero/herotree.png";
    }

    userStage.setHeroName(heroName);
    userStage.setHeroUrl(heroUrl);
    stageRepository.save(userStage);
  }

  @Override
  @Transactional
  public HeroStageResponse getHeroStageMessage(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    Stage userStage = stageRepository.findByUser(user)
        .orElseGet(() -> stageRepository.save(Stage.builder()
            .user(user)
            .treeName("씨앗")
            .treeUrl("https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/seed.png")
            .heroName("Lv.1 새싹지기")
            .heroUrl("https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/hero/heroseed.png")
            .build()));

    String heroName = userStage.getHeroName();
    String heroUrl = userStage.getHeroUrl();
    if (heroName == null) {
      throw new IllegalArgumentException("사용자의 영웅 단계 정보가 없습니다");
    }

    return HeroStageResponse.builder()
        .heroName(heroName)
        .heroUrl(heroUrl)
        .build();
  }
}
