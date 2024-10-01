package com.coopang.user.domain.repository;

import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.presentation.request.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(UUID userId);

    Page<UserEntity> search(UserSearchCondition condition, Pageable pageable);

    Page<UserEntity> findAll(Pageable pageable);

    Page<UserEntity> findAllByIsDeletedFalse(Pageable pageable);
}