package com.coopang.ainoti.infrastructure.repository.slackMessage;

import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageEntity;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlackMessageRepositoryCustom {

    Page<SlackMessageEntity> search(Pageable pageable);
    Page<SlackMessageEntity> searchWithCondition(SlackMessageSearchConditionDto conditionDto,Pageable pageable);
}
