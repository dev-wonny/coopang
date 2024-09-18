package com.coopang.user.infrastructure.repository;

import com.coopang.user.domain.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID>, UserRepositoryCustom {
    boolean existsByEmail(String email);

    default UserEntity findUserByIdOrThrowException(UUID userId) {
        return findById(userId)
                .filter(user -> !user.isDeleted())  // 삭제되지 않은 유저인지 확인
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or has been deleted"));
    }

    Optional<UserEntity> findUserEntityByUserId(UUID userId);


    Optional<UserEntity> findByEmail(String email);// JpaRepository-provided method
}
