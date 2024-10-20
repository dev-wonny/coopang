package com.coopang.ainoti.presentation.request.noti;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateSlackMessageRequestDto {

    @NotNull(message = "Slack Message ID is required")
    private UUID slackMessageId; // 필수: 메시지 ID

    @NotBlank(message = "Receiver Slack ID is required")
    private String receiveSlackId; // 필수: 수신자의 Slack ID

    @NotNull(message = "Receiver User ID is required")
    private UUID receiveUserId; // 필수: 수신자의 UUID

    @NotBlank(message = "Slack message content is required")
    private String slackMessage; // 필수: 메시지 내용

    @NotNull(message = "Sent time is required")
    private LocalDateTime sentTime; // 필수: 전송 시간

    @NotBlank(message = "Sender Slack ID is required")
    private String slackMessageSenderId; // 필수: 발신자 ID
}