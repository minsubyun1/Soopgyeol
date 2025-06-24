package com.soopgyeol.api.repository;

import com.soopgyeol.api.domain.user.SocialLoginType;
import com.soopgyeol.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndProvider(String socialId, SocialLoginType provider);

    Optional<User> findByEmail(String email);
}