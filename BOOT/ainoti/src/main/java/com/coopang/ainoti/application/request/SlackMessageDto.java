package com.coopang.ainoti.application.request;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationStatus;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlackMessageDto {

    private String receiveSlackId;
    private UUID receiveUserId;
    private String slackMessage;
    private String slackMessageSenderId;
    private SlackNotificationStatus slackNotificationStatus;
}
