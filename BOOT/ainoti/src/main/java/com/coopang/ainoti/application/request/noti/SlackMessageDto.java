package com.coopang.ainoti.application.request.noti;

import com.coopang.ainoti.application.enums.SlackMessageStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class SlackMessageDto {
    private UUID slackMessageId;
    private String receiveSlackId;// 선택적 필드
    private UUID receiveUserId;
    private SlackMessageStatus slackMessageStatus;
    private String slackMessage;
    private LocalDateTime sentTime;
    private String slackMessageSenderId;// 선택적 필드

    @Builder
    private SlackMessageDto(
        UUID slackMessageId
        , String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTime
        , String slackMessageSenderId
    ) {
        this.slackMessageId = slackMessageId;
        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.sentTime = sentTime;
        this.slackMessageSenderId = slackMessageSenderId;
    }

    public static SlackMessageDto from(
        UUID slackMessageId
        , String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTime
        , String slackMessageSenderId
    ) {
        return SlackMessageDto.builder()
            .slackMessageId(slackMessageId)
            .receiveSlackId(receiveSlackId)
            .receiveUserId(receiveUserId)// 선택적 필드
            .slackMessageStatus(slackMessageStatus)
            .slackMessage(slackMessage)
            .sentTime(sentTime)
            .slackMessageSenderId(slackMessageSenderId)// 선택적 필드
            .build();
    }

    @Override
    public String toString() {
        return "SlackMessageDto{" +
            "slackMessageId=" + slackMessageId +
            ", receiveSlackId='" + receiveSlackId + '\'' +
            ", receiveUserId=" + receiveUserId +
            ", slackMessageStatus=" + slackMessageStatus +
            ", slackMessage='" + slackMessage + '\'' +
            ", sentTime=" + sentTime +
            ", slackMessageSenderId='" + slackMessageSenderId + '\'' +
            '}';
    }

    public void createId(UUID slackMessageId) {
        this.slackMessageId = slackMessageId;
    }
}