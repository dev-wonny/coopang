package com.coopang.ainoti.infrastructure.repository.ai;

import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import com.coopang.ainoti.domain.repository.ai.AiRequestHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiRequestHistoryJpaRepository extends JpaRepository<AiRequestHistoryEntity, UUID>, AiRequestHistoryRepository, AiRequestHistoryRepositoryCustom {
}
