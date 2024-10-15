package com.coopang.ainoti.application.response;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationEntity;
import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class SlackNotificationResponseDto {

    private UUID slackMessageId;
    private String receiveSlackId;
    private UUID receiveUserId;
    private SlackNotificationStatus slackNotificationStatus;
    private String slackMessage;
    private LocalDateTime sentTime;
    private String slackMessageSenderId;

    public static SlackNotificationResponseDto of(SlackNotificationEntity slackNotificationEntity) {
        return SlackNotificationResponseDto.builder()
            .slackMessageId(slackNotificationEntity.getSlackMessageId())
            .receiveSlackId(slackNotificationEntity.getReceiveSlackId())
            .receiveUserId(slackNotificationEntity.getReceiveUserId())
            .slackNotificationStatus(slackNotificationEntity.getSlackNotificationStatus())
            .slackMessage(slackNotificationEntity.getSlackMessage())
            .sentTime(slackNotificationEntity.getSentTime())
            .slackMessageSenderId(slackNotificationEntity.getSlackMessageSenderId())
            .build();
    }
}
