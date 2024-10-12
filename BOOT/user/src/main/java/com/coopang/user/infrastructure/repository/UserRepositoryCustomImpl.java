package com.coopang.user.infrastructure.repository;


import static com.coopang.user.domain.entity.user.QUserEntity.userEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.user.application.request.UserSearchCondition;
import com.coopang.user.domain.entity.user.UserEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryCustomImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom {

    public UserRepositoryCustomImpl() {
        super(UserEntity.class);
    }

    @Override
    public Page<UserEntity> search(UserSearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(userEntity)
                        .where(
                                userIdEq(condition.getUserId())
                                , userNameEq(condition.getUserName())
                                , emailEq(condition.getEmail())
                                , roleEq(condition.getUserRole())
                                , userEntity.isDeleted.eq(condition.getIsDeleted())
                        ),
                countQuery -> countQuery
                        .selectFrom(userEntity)
                        .where(
                                userIdEq(condition.getUserId())
                                , userNameEq(condition.getUserName())
                                , emailEq(condition.getEmail())
                                , roleEq(condition.getUserRole())
                                , userEntity.isDeleted.eq(condition.getIsDeleted())
                        )
        );
    }

    @Override
    public List<UserEntity> findUserList(UserSearchCondition condition) {
        return selectFrom(userEntity)
                .where(
                        userIdEq(condition.getUserId())
                        , userNameEq(condition.getUserName())
                        , emailEq(condition.getEmail())
                        , roleEq(condition.getUserRole())
                        , userEntity.isDeleted.eq(condition.getIsDeleted())
                )
                .fetch();
    }

    private Predicate userIdEq(UUID userId) {
        return userId != null ? userEntity.userId.eq(userId) : null;
    }

    private Predicate emailEq(String email) {
        return StringUtils.hasText(email) ? userEntity.email.eq(email) : null;
    }

    private Predicate userNameEq(String userName) {
        return StringUtils.hasText(userName) ? userEntity.userName.eq(userName) : null;
    }

    private Predicate roleEq(UserRoleEnum userRole) {
        return userRole != null ? userEntity.role.eq(userRole) : null;
    }
}
