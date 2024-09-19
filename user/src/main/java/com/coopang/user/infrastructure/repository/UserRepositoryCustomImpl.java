package com.coopang.user.infrastructure.repository;


import static com.coopang.user.domain.entity.user.QUserEntity.userEntity;

import com.coopang.user.application.enums.UserRoleEnum;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.presentation.request.UserSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserEntity> search(UserSearchCondition condition, Pageable pageable) {
        List<UserEntity> users = queryFactory
                .select(userEntity)
                .from(userEntity)
                .where(
                        usernameEq(condition.getUserName()),
                        emailEq(condition.getEmail()),
                        roleEq(condition.getUserRole())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable))
                .fetch();

        long total = queryFactory
                .select(userEntity.count())
                .from(userEntity)
                .where(
                        usernameEq(condition.getUserName()),
                        emailEq(condition.getEmail()),
                        roleEq(condition.getUserRole())
                )
                .fetchOne();

        return new PageImpl<>(users, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return new OrderSpecifier<?>[] {userEntity.createdAt.desc()}; // 기본 정렬 기준
        }

        // 정렬 조건을 직접 생성합니다.
        OrderSpecifier<?>[] orderSpecifiers = pageable.getSort().stream()
                .map(order -> {
                    if (order.getProperty().equals("createdAt")) {
                        return order.isAscending() ? userEntity.createdAt.asc() : userEntity.createdAt.desc();
                    }
                    if (order.getProperty().equals("updatedAt")) {
                        return order.isAscending() ? userEntity.updatedAt.asc() : userEntity.updatedAt.desc();
                    }
                    // 다른 정렬 기준 추가 가능
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);

        // 정렬 조건이 없는 경우 기본 정렬 기준을 사용합니다.
        return orderSpecifiers.length > 0 ? orderSpecifiers : new OrderSpecifier<?>[] {userEntity.createdAt.desc()};
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
}
