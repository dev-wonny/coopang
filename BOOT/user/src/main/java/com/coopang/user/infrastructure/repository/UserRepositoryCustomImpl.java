package com.coopang.user.infrastructure.repository;


import static com.coopang.user.domain.entity.user.QUserEntity.userEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.user.application.request.UserSearchConditionDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryCustomImpl extends Querydsl4RepositorySupport implements UserRepositoryCustom {

    public UserRepositoryCustomImpl() {
        super(UserEntity.class);
    }

    @Override
    public Page<UserEntity> search(UserSearchConditionDto condition, Pageable pageable) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(userEntity)
                .where(
                    whereClause
                ),
            countQuery -> countQuery
                .selectFrom(userEntity)
                .where(
                    whereClause
                )
        );
    }

    @Override
    public List<UserEntity> findUserList(UserSearchConditionDto condition) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return selectFrom(userEntity)
            .where(
                whereClause
            )
            .fetch();
    }

    private BooleanBuilder generateWhereClause(UserSearchConditionDto condition) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(userIdEq(condition.getUserId()));
        whereClause.and(userNameEq(condition.getUserName()));
        whereClause.and(emailEq(condition.getEmail()));
        whereClause.and(roleEq(condition.getUserRole()));
        whereClause.and(userEntity.isDeleted.eq(condition.isDeleted()));
        return whereClause;
    }

    private Predicate userIdEq(UUID userId) {
        return !ObjectUtils.isEmpty(userId) ? userEntity.userId.eq(userId) : null;
    }

    private Predicate emailEq(String email) {
        return StringUtils.hasText(email) ? userEntity.email.eq(email) : null;
    }

    private Predicate userNameEq(String userName) {
        return StringUtils.hasText(userName) ? userEntity.userName.eq(userName) : null;
    }

    private Predicate roleEq(UserRoleEnum userRole) {
        return !ObjectUtils.isEmpty(userRole) ? userEntity.role.eq(userRole) : null;
    }
}
