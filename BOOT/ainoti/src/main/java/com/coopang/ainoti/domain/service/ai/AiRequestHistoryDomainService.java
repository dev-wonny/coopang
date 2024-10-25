package com.coopang.ainoti.domain.service.ai;

import com.coopang.ainoti.application.request.ai.AiRequestHistoryDto;
import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import com.coopang.ainoti.infrastructure.repository.ai.AiRequestHistoryJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AiRequestHistoryDomainService {

    private final AiRequestHistoryJpaRepository aiRequestHistoryJpaRepository;

    public AiRequestHistoryDomainService(AiRequestHistoryJpaRepository aiRequestHistoryJpaRepository) {
        this.aiRequestHistoryJpaRepository = aiRequestHistoryJpaRepository;
    }

    public AiRequestHistoryEntity createAiRequestHistory(AiRequestHistoryDto aiRequestHistoryDto) {
        AiRequestHistoryEntity aiRequestHistoryEntity = AiRequestHistoryEntity.create(
            aiRequestHistoryDto.getAiRequestHistoryId(),
            aiRequestHistoryDto.getAiCategory(),
            aiRequestHistoryDto.getAiRequest(),
            aiRequestHistoryDto.getAiResponse()
        );
        return aiRequestHistoryJpaRepository.save(aiRequestHistoryEntity);
    }
}
