package com.coopang.ainoti.domain.repository.slackMessage;

import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageEntity;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlackMessageRepository {

    Optional<SlackMessageEntity> findBySlackMessageIdAndIsDeletedFalse(UUID slackMessageId);

    Page<SlackMessageEntity> search(Pageable pageable);

    Page<SlackMessageEntity> searchWithCondition(SlackMessageSearchConditionDto conditionDto,Pageable pageable);
}
