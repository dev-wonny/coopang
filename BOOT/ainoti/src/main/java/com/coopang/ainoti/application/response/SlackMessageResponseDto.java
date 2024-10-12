package com.coopang.ainoti.application.response;

import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageEntity;
import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SlackMessageResponseDto(
    UUID slackMessageId,
    String receiveSlackId,
    UUID receiveUserId,
    SlackMessageStatus slackMessageStatus,
    String slack_message,
    LocalDateTime sentTime,
    String slackMessageSenderId
) {

    public static SlackMessageResponseDto of(SlackMessageEntity slackMessageEntity) {
        return SlackMessageResponseDto.builder()
            .slackMessageId(slackMessageEntity.getSlackMessageId())
            .receiveSlackId(slackMessageEntity.getReceiveSlackId())
            .receiveUserId(slackMessageEntity.getReceiveUserId())
            .slackMessageStatus(slackMessageEntity.getSlackMessageStatus())
            .slack_message(slackMessageEntity.getSlackMessage())
            .sentTime(slackMessageEntity.getSentTime())
            .slackMessageSenderId(slackMessageEntity.getSlackMessageSenderId())
            .build();
    }
}
