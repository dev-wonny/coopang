package com.coopang.ainoti.application.request.noti;

import com.coopang.ainoti.application.enums.SlackMessageStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SlackMessageDto {
    private UUID slackMessageId;
    private String receiveSlackId;
    private UUID receiveUserId;
    private SlackMessageStatus slackMessageStatus;
    private String slackMessage;
    private LocalDateTime sentTime;
    private String slackMessageSenderId;
}