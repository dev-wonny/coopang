package com.coopang.ainoti.application.response.noti;


import com.coopang.ainoti.application.enums.SlackMessageStatus;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class SlackMessageResponseDto {
    private UUID slackMessageId;
    private String receiveSlackId;
    private UUID receiveUserId;
    private SlackMessageStatus slackMessageStatus;
    private String slackMessage;
    private LocalDateTime sentTime;
    private String slackMessageSenderId;
    private boolean isDeleted;

    @Builder
    private SlackMessageResponseDto(
        UUID slackMessageId
        , String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTime
        , String slackMessageSenderId
        , boolean isDeleted
    ) {
        this.slackMessageId = slackMessageId;
        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.sentTime = sentTime;
        this.slackMessageSenderId = slackMessageSenderId;
        this.isDeleted = isDeleted;
    }

    public static SlackMessageResponseDto fromSlackMessage(SlackMessageEntity slackMessageEntity) {
        return SlackMessageResponseDto.builder()
            .slackMessageId(slackMessageEntity.getSlackMessageId())
            .receiveSlackId(slackMessageEntity.getReceiveSlackId())
            .receiveUserId(slackMessageEntity.getReceiveUserId())
            .slackMessageStatus(slackMessageEntity.getSlackMessageStatus())
            .slackMessage(slackMessageEntity.getSlackMessage())
            .sentTime(slackMessageEntity.getSentTime())
            .slackMessageSenderId(slackMessageEntity.getSlackMessageSenderId())
            .isDeleted(slackMessageEntity.isDeleted())
            .build();
    }
}
