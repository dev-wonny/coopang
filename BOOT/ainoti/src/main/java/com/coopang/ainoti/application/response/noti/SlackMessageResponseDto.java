package com.coopang.ainoti.application.response.noti;


import com.coopang.ainoti.application.enums.SlackMessageStatus;
import com.coopang.ainoti.domain.entity.noti.SlackMessageEntity;
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

    private SlackMessageResponseDto(
        UUID slackMessageId,
        String receiveSlackId,
        UUID receiveUserId,
        SlackMessageStatus slackMessageStatus,
        String slackMessage,
        LocalDateTime sentTime,
        String slackMessageSenderId,
        boolean isDeleted
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

    /**
     * SlackMessageEntity를 SlackMessageResponseDto로 변환
     *
     * @param slackMessageEntity Slack 메시지 엔티티
     * @return SlackMessageResponseDto
     */
    public static SlackMessageResponseDto fromSlackMessage(SlackMessageEntity slackMessageEntity) {
        return new SlackMessageResponseDto(
            slackMessageEntity.getSlackMessageId(),
            slackMessageEntity.getReceiveSlackId(),
            slackMessageEntity.getReceiveUserId(),
            slackMessageEntity.getSlackMessageStatus(),
            slackMessageEntity.getSlackMessage(),
            slackMessageEntity.getSentTime(),
            slackMessageEntity.getSlackMessageSenderId(),
            slackMessageEntity.isDeleted()
        );
    }
}
