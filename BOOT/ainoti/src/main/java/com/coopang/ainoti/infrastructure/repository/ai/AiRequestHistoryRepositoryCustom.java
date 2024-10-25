package com.coopang.ainoti.infrastructure.repository.ai;

import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AiRequestHistoryRepositoryCustom {
    Page<AiRequestHistoryEntity> search(AiRequestHistorySearchConditionDto condition, Pageable pageable);

    List<AiRequestHistoryEntity> findAiRequestHistoryList(AiRequestHistorySearchConditionDto condition);
}
