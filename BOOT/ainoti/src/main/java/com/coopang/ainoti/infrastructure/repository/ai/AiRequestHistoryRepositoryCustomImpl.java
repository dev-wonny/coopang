package com.coopang.ainoti.infrastructure.repository.ai;

import static com.coopang.ainoti.domain.entity.ai.QAiRequestHistoryEntity.aiRequestHistoryEntity;

import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.apidata.application.ai.AiCategory;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class AiRequestHistoryRepositoryCustomImpl extends Querydsl4RepositorySupport implements AiRequestHistoryRepositoryCustom {

    public AiRequestHistoryRepositoryCustomImpl() {
        super(AiRequestHistoryEntity.class);
    }

    @Override
    public Page<AiRequestHistoryEntity> search(AiRequestHistorySearchConditionDto condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(aiRequestHistoryEntity)
                .where(
                    aiCategoryEq(condition.getAiCategory())
                    , aiRequestContains(condition.getAiRequest())
                    , aiRequestHistoryEntity.isDeleted.eq(condition.isDeleted())

                ),
            countQuery -> countQuery
                .selectFrom(aiRequestHistoryEntity)
                .where(
                    aiCategoryEq(condition.getAiCategory())
                    , aiRequestContains(condition.getAiRequest())
                    , aiRequestHistoryEntity.isDeleted.eq(condition.isDeleted())

                )
        );
    }

    @Override
    public List<AiRequestHistoryEntity> findAiRequestHistoryList(AiRequestHistorySearchConditionDto condition) {
        return selectFrom(aiRequestHistoryEntity)
            .where(
                aiCategoryEq(condition.getAiCategory())
                , aiRequestContains(condition.getAiRequest())
                , aiRequestHistoryEntity.isDeleted.eq(condition.isDeleted())

            )
            .fetch();
    }

    private Predicate aiCategoryEq(AiCategory aiCategory) {
        return !ObjectUtils.isEmpty(aiCategory) ? aiRequestHistoryEntity.aiCategory.eq(aiCategory) : null;
    }

    private Predicate aiRequestContains(String aiRequest) {
        return StringUtils.hasText(aiRequest) ? aiRequestHistoryEntity.aiRequest.contains(aiRequest) : null;
    }

}