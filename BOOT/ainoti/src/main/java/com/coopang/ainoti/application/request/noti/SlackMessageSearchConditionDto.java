package com.coopang.ainoti.application.request.noti;

import com.coopang.ainoti.application.enums.SlackMessageStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SlackMessageSearchConditionDto {

    private UUID slackMessageId;            // 슬랙 메시지 고유 ID
    private String receiveSlackId;          // 수신자 슬랙 ID (startsWith 조건)
    private UUID receiveUserId;             // 수신자 사용자 ID
    private SlackMessageStatus slackMessageStatus; // 슬랙 메시지 상태
    private String slackMessage;            // 메시지 내용 (contains 조건)
    private LocalDateTime sentTimeFrom;     // 발송 시간 시작 범위
    private LocalDateTime sentTimeTo;       // 발송 시간 끝 범위
    private boolean isDeleted = false;      // 삭제 여부
}