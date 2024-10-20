package com.coopang.ainoti.domain.service.noti;

import com.coopang.ainoti.application.request.noti.SlackMessageDto;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import com.coopang.ainoti.infrastructure.repository.noti.SlackMessageJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SlackMessageDomainService {

    private final SlackMessageJpaRepository slackMessageJpaRepository;

    public SlackMessageDomainService(SlackMessageJpaRepository slackMessageJpaRepository) {
        this.slackMessageJpaRepository = slackMessageJpaRepository;
    }

    public SlackMessageEntity createSlackMessage(SlackMessageDto slackMessageDto) {
        SlackMessageEntity slackMessageEntity = SlackMessageEntity.create(
            slackMessageDto.getSlackMessageId(),
            slackMessageDto.getReceiveSlackId(),
            slackMessageDto.getReceiveUserId(),
            slackMessageDto.getSlackMessageStatus(),
            slackMessageDto.getSlackMessage(),
            slackMessageDto.getSentTime(),
            slackMessageDto.getSlackMessageSenderId()
        );
        return slackMessageJpaRepository.save(slackMessageEntity);
    }
}
