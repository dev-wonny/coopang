package com.coopang.user.infrastructure.repository;


import static com.coopang.user.domain.entity.user.QUserEntity.userEntity;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.domain.repository.UserRepository;
import com.coopang.user.presentation.request.UserSearchCondition;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom, UserRepository {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<UserEntity> search(UserSearchCondition condition) {
        return queryFactory
                .select(userEntity)
                .from(userEntity)
                .where(
                        usernameEq(condition.getUserName()),
                        emailEq(condition.getEmail()),
                        roleEq(condition.getUserRole())
                )
                .fetch()
                ;
    }

    private Predicate emailEq(String email) {
        return StringUtils.hasText(email) ? userEntity.email.eq(email) : null;
    }

    private Predicate usernameEq(String userName) {
        return StringUtils.hasText(userName) ? userEntity.username.eq(userName) : null;
    }

    private Predicate roleEq(String userRole) {
        return StringUtils.hasText(userRole) ? userEntity.role.eq(UserRoleEnum.valueOf(userRole)) : null;
    }


    @Override
    public Optional<UserEntity> findByEmail(String email) {
        UserEntity user = queryFactory
                .selectFrom(userEntity)
                .where(userEntity.email.eq(email))
                .fetchOne();
        return Optional.ofNullable(user);
    }
}
