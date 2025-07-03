package com.soopgyeol.api.service.stage;

import com.soopgyeol.api.domain.stage.dto.TreeStageResponse;
import com.soopgyeol.api.domain.stage.entity.Stage;
import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.repository.StageRepository;
import com.soopgyeol.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TreeStageServiceImpl implements TreeStageService {
  private final UserRepository userRepository;
  private final StageRepository stageRepository;

  @Override
  @Transactional
  public void updateTreeStageByGrowth(Long userId) {
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
    String treeName;
    String treeUrl;
    if (growth <= 100) {
      treeName = "씨앗";
      treeUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/seed.png";
    } else if (growth <= 300) {
      treeName = "새싹";
      treeUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/sappling.png";
    } else if (growth <= 700) {
      treeName = "작은 나무";
      treeUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/littletree.png";
    } else {
      treeName = "나무";
      treeUrl = "https://soopgyeolbucket.s3.ap-northeast-2.amazonaws.com/tree.png";
    }

    userStage.setTreeName(treeName);
    userStage.setTreeUrl(treeUrl);
    stageRepository.save(userStage);
  }

  @Override
  @Transactional
  public TreeStageResponse getTreeStageMessage(Long userId) {
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

    String treeName = userStage.getTreeName();
    String treeUrl = userStage.getTreeUrl();
    if (treeName == null) {
      throw new IllegalArgumentException("사용자의 나무 단계 정보가 없습니다");
    }

    return TreeStageResponse.builder()
        .treeName(treeName)
        .treeUrl(treeUrl)
        .build();
  }
}
