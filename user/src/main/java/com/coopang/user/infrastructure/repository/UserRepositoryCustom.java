package com.coopang.user.infrastructure.repository;

import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.presentation.request.UserSearchCondition;

import java.util.List;

public interface UserRepositoryCustom {
    List<UserEntity> search(UserSearchCondition condition);
}
