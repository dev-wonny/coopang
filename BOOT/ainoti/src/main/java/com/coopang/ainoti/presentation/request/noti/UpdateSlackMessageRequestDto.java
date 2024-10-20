package com.coopang.ainoti.presentation.request.noti;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateSlackMessageRequestDto {

    @NotNull
    private UUID slackMessageId; // 필수: 업데이트할 Slack 메시지의 UUID

    @NotBlank
    private String receiveSlackId; // 필수: 수신자의 Slack ID

    @NotNull
    private UUID receiveUserId; // 필수: 수신자의 UUID

    @NotBlank
    private String slackMessage; // 필수: 새로운 메시지 내용

    @NotNull
    private LocalDateTime sentTime; // 필수: 메시지 전송 시간

    @NotBlank
    private String slackMessageSenderId; // 필수: 발신자 Slack ID
}