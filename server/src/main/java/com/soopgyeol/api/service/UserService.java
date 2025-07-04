package com.soopgyeol.api.service;

import com.soopgyeol.api.domain.user.User;
import com.soopgyeol.api.domain.user.dto.MoneyBalanceResponse;
import com.soopgyeol.api.repository.UserRepository;
import com.soopgyeol.api.repository.StageRepository;
import com.soopgyeol.api.repository.InventoryRepository;
import com.soopgyeol.api.repository.UserChallengeRepository;
import com.soopgyeol.api.repository.UserCarbonLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final StageRepository stageRepository;
    private final InventoryRepository inventoryRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserCarbonLogRepository userCarbonLogRepository;

    public String updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        user.setNickname(nickname);
        userRepository.save(user);
        return nickname;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        stageRepository.deleteByUser(user);
        inventoryRepository.deleteByUser(user);
        userChallengeRepository.deleteByUser(user);
        userCarbonLogRepository.deleteByUserId(userId);
        userRepository.delete(user);
    }

    public MoneyBalanceResponse getMoneyBalance(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return new MoneyBalanceResponse(user.getMoneyBalance());
    }

}
