package com.coopang.ainoti.presentation.request;

import com.coopang.ainoti.domain.entitiy.slacknotification.SlackNotificationStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateSlackNotificationRequestDto {

    private String receiveSlackId;
    @NotNull
    private UUID receiveUserId;
    private String slackMessage;
    private String slackMessageSenderId;
    private SlackNotificationStatus slackNotificationStatus;

}
