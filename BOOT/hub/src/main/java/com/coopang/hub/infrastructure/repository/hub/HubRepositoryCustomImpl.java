package com.coopang.hub.infrastructure.repository.hub;

import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.hub.application.request.hub.HubSearchConditionDto;
import com.coopang.hub.domain.entity.hub.HubEntity;
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
public class HubRepositoryCustomImpl extends Querydsl4RepositorySupport implements HubRepositoryCustom {

    public HubRepositoryCustomImpl() {
        super(HubEntity.class);
    }

    @Override
    public Page<HubEntity> search(HubSearchConditionDto condition, Pageable pageable) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(hubEntity)
                .where(
                    whereClause
                ),
            countQuery -> countQuery
                .selectFrom(hubEntity)
                .where(
                    whereClause
                )
        );
    }

    @Override
    public List<HubEntity> findHubList(HubSearchConditionDto condition) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return selectFrom(hubEntity)
            .where(
                whereClause
            )
            .fetch();
    }

    private BooleanBuilder generateWhereClause(HubSearchConditionDto condition) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(hubIdEq(condition.getHubId()));
        whereClause.and(hubNameStarsWith(condition.getHubName()));
        whereClause.and(hubManagerIdEq(condition.getHubManagerId()));
        whereClause.and(hubEntity.isDeleted.eq(condition.isDeleted()));
        return whereClause;
    }

    private Predicate hubIdEq(UUID hubId) {
        return !ObjectUtils.isEmpty(hubId) ? hubEntity.hubId.eq(hubId) : null;
    }

    private Predicate hubNameStarsWith(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.startsWith(hubName) : null;
    }

    private Predicate hubManagerIdEq(UUID hubManagerId) {
        return !ObjectUtils.isEmpty(hubManagerId) ? hubEntity.hubManagerId.eq(hubManagerId) : null;
    }
}
