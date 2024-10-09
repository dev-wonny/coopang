package com.coopang.hub.infrastructure.repository.hub;

import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.hub.application.request.hub.HubSearchCondition;
import com.coopang.hub.domain.entity.hub.HubEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
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
                        .where(
                                hubNameStarsWith(condition.getHubName())
                                , hubIdEq(condition.getHubId())
                                , hubManagerIdEq(condition.getHubManagerId())
                                , hubEntity.isDeleted.eq(condition.getIsDeleted())
                        ),
                countQuery -> countQuery
                        .selectFrom(hubEntity)
                        .where(
                                hubNameStarsWith(condition.getHubName())
                                , hubIdEq(condition.getHubId())
                                , hubManagerIdEq(condition.getHubManagerId())
                                , hubEntity.isDeleted.eq(condition.getIsDeleted())
                        )
        );
    }

    @Override
    public List<HubEntity> findHubList(HubSearchCondition condition) {
        return selectFrom(hubEntity)
                .where(
                        hubNameStarsWith(condition.getHubName()),
                        hubIdEq(condition.getHubId()),
                        hubManagerIdEq(condition.getHubManagerId()),
                        hubEntity.isDeleted.eq(condition.getIsDeleted())
                )
                .fetch();
    }

    private Predicate hubNameStarsWith(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.startsWith(hubName) : null;
    }

    private Predicate hubIdEq(UUID hubId) {
        return hubId != null ? hubEntity.hubId.eq(hubId) : null;
    }

    private Predicate hubManagerIdEq(UUID hubManagerId) {
        return hubManagerId != null ? hubEntity.hubManagerId.eq(hubManagerId) : null;
    }
}
