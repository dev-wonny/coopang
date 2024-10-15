package com.coopang.ainoti.domain.repository.slacknotification;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlackNotificationRepository {

    Optional<SlackNotificationEntity> findBySlackMessageIdAndIsDeletedFalse(UUID slackMessageId);

    Page<SlackNotificationEntity> search(Pageable pageable);

    Page<SlackNotificationEntity> searchWithCondition(SlackMessageSearchConditionDto conditionDto,Pageable pageable);
}
