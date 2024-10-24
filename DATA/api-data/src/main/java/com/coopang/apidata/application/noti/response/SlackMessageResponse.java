package com.coopang.apidata.application.noti.response;

import com.coopang.apidata.application.noti.enums.SlackMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageResponse {
    private UUID slackMessageId;
    private String receiveSlackId;
    private UUID receiveUserId;
    private SlackMessageStatus slackMessageStatus;
    private String slackMessage;
    private LocalDateTime sentTime;
    private String slackMessageSenderId;
    private boolean isDeleted;
}
