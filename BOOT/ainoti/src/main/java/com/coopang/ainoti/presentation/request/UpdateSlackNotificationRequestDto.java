package com.coopang.ainoti.presentation.request;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationStatus;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateSlackNotificationRequestDto {

    private String receiveSlackId;
    private UUID receiveUserId;
    private SlackNotificationStatus slackNotificationStatus;
    private String slackMessage;
    private String slackMessageSenderId;

}
