package com.coopang.ainoti.presentation.request.noti;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SlackMessageSearchConditionRequestDto {
    private UUID slackMessageId;            // 슬랙 메시지 고유 ID
    private String receiveSlackId;          // 수신자 슬랙 ID (startsWith 조건)
    private UUID receiveUserId;             // 수신자 사용자 ID
    private String slackMessageStatus; // 슬랙 메시지 상태
    private String slackMessage;            // 메시지 내용 (contains 조건)
    private String sentTimeFrom;     // 발송 시간 시작 범위
    private String sentTimeTo;       // 발송 시간 끝 범위
    private boolean isDeleted = false;      // 삭제 여부
}