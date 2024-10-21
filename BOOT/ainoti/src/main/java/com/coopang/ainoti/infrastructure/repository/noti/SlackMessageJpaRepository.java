package com.coopang.ainoti.infrastructure.repository.noti;

import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import com.coopang.ainoti.domain.repository.noti.SlackMessageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SlackMessageJpaRepository extends JpaRepository<SlackMessageEntity, UUID>, SlackMessageRepository, SlackMessageRepositoryCustom {
    Optional<SlackMessageEntity> findById(UUID slackMessageId);
}
