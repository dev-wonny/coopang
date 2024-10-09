package com.coopang.user.infrastructure.repository;

import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID>, UserRepository, UserRepositoryCustom {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
