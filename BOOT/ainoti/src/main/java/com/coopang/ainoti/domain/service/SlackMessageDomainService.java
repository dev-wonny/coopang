package com.coopang.ainoti.domain.service;

import com.coopang.ainoti.application.request.SlackMessageDto;
import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageEntity;
import com.coopang.ainoti.infrastructure.repository.slackMessage.SlackMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SlackMessageDomainService {

    private final SlackMessageJpaRepository slackMessageRepository;

    public SlackMessageEntity createMessage(SlackMessageDto slackMessageDto){

        return slackMessageRepository.save( slackMessageDtoToEntity(slackMessageDto));
    }

    private SlackMessageEntity slackMessageDtoToEntity(SlackMessageDto slackMessageDto){
        return SlackMessageEntity.create(
            null,
            slackMessageDto.getReceiveSlackId(),
            slackMessageDto.getReceiveUserId(),
            slackMessageDto.getSlackMessageStatus(),
            slackMessageDto.getSlackMessage(),
            slackMessageDto.getSlackMessageSenderId()

        );
    }
}
