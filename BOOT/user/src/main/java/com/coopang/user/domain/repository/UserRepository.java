package com.coopang.user.domain.repository;

import com.coopang.user.application.request.UserSearchConditionDto;
import com.coopang.user.domain.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(UUID userId);

    Optional<UserEntity> findByUserIdAndIsDeletedFalseAndIsBlockFalse(UUID userId);

    Page<UserEntity> search(UserSearchConditionDto condition, Pageable pageable);
    List<UserEntity> findUserList(UserSearchConditionDto condition);

    Page<UserEntity> findAll(Pageable pageable);
}