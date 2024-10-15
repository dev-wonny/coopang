package com.coopang.ainoti.domain.service;

import com.coopang.ainoti.application.request.SlackMessageDto;
import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.infrastructure.repository.slacknotification.SlackNotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SlackNotificationDomainService {

    private final SlackNotificationJpaRepository slackMessageRepository;

    public SlackNotificationEntity createMessage(SlackMessageDto slackMessageDto){

        return slackMessageRepository.save( slackMessageDtoToEntity(slackMessageDto));
    }

    private SlackNotificationEntity slackMessageDtoToEntity(SlackMessageDto slackMessageDto){
        return SlackNotificationEntity.create(
            null,
            slackMessageDto.getReceiveSlackId(),
            slackMessageDto.getReceiveUserId(),
            slackMessageDto.getSlackNotificationStatus(),
            slackMessageDto.getSlackMessage(),
            slackMessageDto.getSlackMessageSenderId()

        );
    }
}
