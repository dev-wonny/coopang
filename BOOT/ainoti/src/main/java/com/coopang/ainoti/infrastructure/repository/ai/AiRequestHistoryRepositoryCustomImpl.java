package com.coopang.ainoti.infrastructure.repository.ai;

import static com.coopang.ainoti.domain.entity.ai.QAiRequestHistoryEntity.aiRequestHistoryEntity;

import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.apidata.application.ai.AiCategory;
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
public class AiRequestHistoryRepositoryCustomImpl extends Querydsl4RepositorySupport implements AiRequestHistoryRepositoryCustom {

    public AiRequestHistoryRepositoryCustomImpl() {
        super(AiRequestHistoryEntity.class);
    }

    @Override
    public Page<AiRequestHistoryEntity> search(AiRequestHistorySearchConditionDto condition, Pageable pageable) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(aiRequestHistoryEntity)
                .where(
                    whereClause
                ),
            countQuery -> countQuery
                .selectFrom(aiRequestHistoryEntity)
                .where(
                    whereClause
                )
        );
    }

    @Override
    public List<AiRequestHistoryEntity> findAiRequestHistoryList(AiRequestHistorySearchConditionDto condition) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return selectFrom(aiRequestHistoryEntity)
            .where(
                whereClause
            )
            .fetch();
    }

    private BooleanBuilder generateWhereClause(AiRequestHistorySearchConditionDto condition) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(aiHistoryIdEq(condition.getAiRequestHistoryId()));
        whereClause.and(aiCategoryEq(condition.getAiCategory()));
        whereClause.and(aiRequestContains(condition.getAiRequest()));
        whereClause.and(aiRequestHistoryEntity.isDeleted.eq(condition.isDeleted()));
        return whereClause;
    }

    private Predicate aiHistoryIdEq(UUID aiRequestHistoryId) {
        return !ObjectUtils.isEmpty(aiRequestHistoryId) ? aiRequestHistoryEntity.aiRequestHistoryId.eq(aiRequestHistoryId) : null;
    }

    private Predicate aiCategoryEq(AiCategory aiCategory) {
        return !ObjectUtils.isEmpty(aiCategory) ? aiRequestHistoryEntity.aiCategory.eq(aiCategory) : null;
    }

    private Predicate aiRequestContains(String aiRequest) {
        return StringUtils.hasText(aiRequest) ? aiRequestHistoryEntity.aiRequest.contains(aiRequest) : null;
    }
}