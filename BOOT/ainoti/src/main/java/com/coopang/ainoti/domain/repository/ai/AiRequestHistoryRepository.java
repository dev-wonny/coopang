package com.coopang.ainoti.domain.repository.ai;

import com.coopang.ainoti.application.request.ai.AiRequestHistorySearchConditionDto;
import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AiRequestHistoryRepository {
    Optional<AiRequestHistoryEntity> findById(UUID aiRequestHistoryId);

    Optional<AiRequestHistoryEntity> findByAiRequestHistoryIdAndIsDeletedFalse(UUID aiRequestHistoryId);

    List<AiRequestHistoryEntity> findAiRequestHistoryList(AiRequestHistorySearchConditionDto condition);

    Page<AiRequestHistoryEntity> search(AiRequestHistorySearchConditionDto condition, Pageable pageable);
}
