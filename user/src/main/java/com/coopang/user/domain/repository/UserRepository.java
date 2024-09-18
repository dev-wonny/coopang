package com.coopang.user.domain.repository;

import com.coopang.user.domain.entity.user.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByEmail(String email);  // Expose the findByEmail method

}
