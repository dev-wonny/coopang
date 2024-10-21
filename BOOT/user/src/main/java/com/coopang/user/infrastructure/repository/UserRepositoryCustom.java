package com.coopang.user.infrastructure.repository;

import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.application.request.UserSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<UserEntity> search(UserSearchConditionDto condition, Pageable pageable);
    List<UserEntity> findUserList(UserSearchConditionDto condition);

}
