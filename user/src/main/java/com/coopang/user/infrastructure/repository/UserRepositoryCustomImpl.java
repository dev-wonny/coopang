package com.coopang.user.infrastructure.repository;


import static com.coopang.user.domain.entity.user.QUserEntity.userEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.apidata.domain.user.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.presentation.request.UserSearchCondition;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class UserRepositoryCustomImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom {

    public UserRepositoryCustomImpl() {
        super(UserEntity.class);
    }

    @Override
    public Page<UserEntity> search(UserSearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(userEntity)
                        .where(usernameEq(condition.getUserName()),
                                emailEq(condition.getEmail()),
                                roleEq(condition.getUserRole())),
                countQuery -> countQuery
                        .selectFrom(userEntity)
                        .where(usernameEq(condition.getUserName()),
                                emailEq(condition.getEmail()),
                                roleEq(condition.getUserRole()))
        );
    }


    private Predicate emailEq(String email) {
        return StringUtils.hasText(email) ? userEntity.email.eq(email) : null;
    }

    private Predicate usernameEq(String userName) {
        return StringUtils.hasText(userName) ? userEntity.username.eq(userName) : null;
    }

    private Predicate roleEq(String userRole) {
        return StringUtils.hasText(userRole) ? userEntity.role.eq(UserRoleEnum.getRoleEnum(userRole)) : null;
    }
}
