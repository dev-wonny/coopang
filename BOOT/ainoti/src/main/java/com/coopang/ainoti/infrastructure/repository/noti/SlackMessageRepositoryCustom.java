package com.coopang.ainoti.infrastructure.repository.noti;

import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SlackMessageRepositoryCustom {
    Page<SlackMessageEntity> search(SlackMessageSearchConditionDto condition, Pageable pageable);

    List<SlackMessageEntity> findSlackMessageList(SlackMessageSearchConditionDto condition);

}
