package com.coopang.apidata.application.noti.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateSlackMessageRequest {
    @NotBlank(message = "Receiver Slack ID is required")
    private String receiveSlackId; // 필수: 수신자의 Slack ID

    private UUID receiveUserId; // 선택: 수신자의 UUID

    @NotNull
    private String slackMessageStatus;

    @NotBlank(message = "Slack message content is required")
    private String slackMessage; // 필수: 메시지 내용

    @NotBlank(message = "Sent time is required")
    private String sentTime; // 필수: 메시지 전송 시간

    private String slackMessageSenderId;// 선택: 발신자 Slack ID
}
