package com.coopang.ainoti.presentation.request;

import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageStatus;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateSlackMessageRequestDto {

    private String receiveSlackId;
    private UUID receiveUserId;
    private SlackMessageStatus slackMessageStatus;
    private String slackMessage;
    private String slackMessageSenderId;

}
