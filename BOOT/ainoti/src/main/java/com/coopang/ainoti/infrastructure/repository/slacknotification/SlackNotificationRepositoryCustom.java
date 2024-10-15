package com.coopang.ainoti.infrastructure.repository.slacknotification;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlackNotificationRepositoryCustom {

    Page<SlackNotificationEntity> search(Pageable pageable);
    Page<SlackNotificationEntity> searchWithCondition(SlackMessageSearchConditionDto conditionDto,Pageable pageable);
}
