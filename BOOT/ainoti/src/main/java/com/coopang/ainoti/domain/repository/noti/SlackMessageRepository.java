package com.coopang.ainoti.domain.repository.noti;

import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlackMessageRepository {
    Optional<SlackMessageEntity> findById(UUID slackMessageId);

    Optional<SlackMessageEntity> findBySlackMessageIdAndIsDeletedFalse(UUID slackMessageId);

    List<SlackMessageEntity> findSlackMessageList(SlackMessageSearchConditionDto condition);

    Page<SlackMessageEntity> search(SlackMessageSearchConditionDto condition, Pageable pageable);
}
