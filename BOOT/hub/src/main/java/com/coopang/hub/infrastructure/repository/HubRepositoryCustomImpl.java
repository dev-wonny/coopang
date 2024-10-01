package com.coopang.hub.infrastructure.repository;

import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.hub.domain.entity.hub.HubEntity;
import com.coopang.hub.presentation.request.HubSearchCondition;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Repository
public class HubRepositoryCustomImpl extends Querydsl4RepositorySupport implements HubRepositoryCustom {


    public HubRepositoryCustomImpl() {
        super(HubEntity.class);
    }

    @Override
    public Page<HubEntity> search(HubSearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(hubEntity)
                        .where(hubNameEq(condition.getHubName()),
                                hubManagerIdEq(condition.getHubManagerId())),
                countQuery -> countQuery
                        .selectFrom(hubEntity)
                        .where(hubNameEq(condition.getHubName()),
                                hubManagerIdEq(condition.getHubManagerId()))
        );
    }

    private Predicate hubNameEq(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.eq(hubName) : null;
    }

    private Predicate hubManagerIdEq(UUID hubManagerId) {
        return StringUtils.hasText(String.valueOf(hubManagerId)) ? hubEntity.hubManagerId.eq(hubManagerId) : null;
    }
}
