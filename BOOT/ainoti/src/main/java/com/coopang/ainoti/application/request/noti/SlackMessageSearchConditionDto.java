package com.coopang.ainoti.application.request.noti;

import com.coopang.ainoti.application.enums.SlackMessageStatus;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class SlackMessageSearchConditionDto {
    private UUID slackMessageId;            // 슬랙 메시지 고유 ID
    private String receiveSlackId;          // 수신자 슬랙 ID (startsWith 조건)
    private UUID receiveUserId;             // 수신자 사용자 ID
    private SlackMessageStatus slackMessageStatus; // 슬랙 메시지 상태
    private String slackMessage;            // 메시지 내용 (contains 조건)
    private LocalDateTime sentTimeFrom;     // 발송 시간 시작 범위
    private LocalDateTime sentTimeTo;       // 발송 시간 끝 범위
    private boolean isDeleted;      // 삭제 여부

    @Builder
    private SlackMessageSearchConditionDto(
        UUID slackMessageId
        , String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTimeFrom
        , LocalDateTime sentTimeTo
        , boolean isDeleted
    ) {
        this.slackMessageId = slackMessageId;
        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.sentTimeFrom = sentTimeFrom;
        this.sentTimeTo = sentTimeTo;
        this.isDeleted = isDeleted;
    }

    public static SlackMessageSearchConditionDto empty() {
        return SlackMessageSearchConditionDto.builder()
            .slackMessageId(null)
            .receiveSlackId(null)
            .receiveUserId(null)
            .slackMessageStatus(null)
            .slackMessage(null)
            .sentTimeFrom(null)
            .sentTimeTo(null)
            .isDeleted(false)
            .build();
    }

    public static SlackMessageSearchConditionDto from(
        UUID slackMessageId
        , String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTimeFrom
        , LocalDateTime sentTimeTo
        , boolean isDeleted
    ) {
        return SlackMessageSearchConditionDto.builder()
            .slackMessageId(slackMessageId)
            .receiveSlackId(receiveSlackId)
            .receiveUserId(receiveUserId)
            .slackMessageStatus(slackMessageStatus)
            .slackMessage(slackMessage)
            .sentTimeFrom(sentTimeFrom)
            .sentTimeTo(sentTimeTo)
            .isDeleted(!ObjectUtils.isEmpty(isDeleted) && isDeleted)
            .build();
    }

    public void setIsDeletedFalse() {
        this.isDeleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SlackMessageSearchConditionDto that = (SlackMessageSearchConditionDto) o;
            return
                Objects.equals(this.slackMessageId, that.slackMessageId)
                    && Objects.equals(this.receiveSlackId, that.receiveSlackId)
                    && Objects.equals(this.receiveUserId, that.receiveUserId)
                    && this.slackMessageStatus == that.slackMessageStatus
                    && Objects.equals(this.slackMessage, that.slackMessage)
                    && Objects.equals(this.sentTimeFrom, that.sentTimeFrom)
                    && Objects.equals(this.sentTimeTo, that.sentTimeTo)
                    && this.isDeleted == that.isDeleted;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.slackMessageId, this.receiveSlackId, this.receiveUserId, this.slackMessageStatus, this.slackMessage, this.sentTimeFrom, this.sentTimeTo, this.isDeleted);
    }

    @Override
    public String toString() {
        return
            "SlackMessageSearchConditionDto(slackMessageId=" + this.slackMessageId
                + ", receiveSlackId=" + this.receiveSlackId
                + ", receiveUserId=" + this.receiveUserId
                + ", slackMessageStatus=" + this.slackMessageStatus
                + ", slackMessage=" + this.slackMessage
                + ", sentTimeFrom=" + this.sentTimeFrom
                + ", sentTimeTo=" + this.sentTimeTo
                + ", isDeleted=" + this.isDeleted
                + ")";
    }
}