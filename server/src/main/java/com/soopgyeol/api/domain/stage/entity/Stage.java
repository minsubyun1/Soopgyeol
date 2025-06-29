package com.soopgyeol.api.domain.stage.entity;


import com.soopgyeol.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stage")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // User와 1:1 관계
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  // 트리 정보
  @Column(nullable = false)
  private String treeName;

  private String treeUrl;

  // 히어로 정보
  @Column(nullable = false)
  private String heroName;

  private String heroUrl;

  public void setUser(User user) {
    this.user = user;
  }

  public void setTreeName(String treeName) {
    this.treeName = treeName;
  }

  public void setTreeUrl(String treeUrl) {
    this.treeUrl = treeUrl;
  }

  public void setHeroName(String heroName) {
    this.heroName = heroName;
  }

  public void setHeroUrl(String heroUrl) {
    this.heroUrl = heroUrl;
  }
}