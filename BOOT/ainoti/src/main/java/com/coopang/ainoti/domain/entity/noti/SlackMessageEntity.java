package com.coopang.ainoti.domain.entity.noti;

import com.coopang.ainoti.application.enums.SlackMessageStatus;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_slack_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class SlackMessageEntity extends BaseEntity {

    @Id
    @Column(name = "slack_message_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID slackMessageId;

    @Column(name = "receive_slack_id", length = 50, nullable = false)
    private String receiveSlackId;

    @Column(name = "receive_user_id", columnDefinition = "UUID")
    private UUID receiveUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "slack_message_status", nullable = false)
    private SlackMessageStatus slackMessageStatus;

    @Column(name = "slack_message", columnDefinition = "TEXT", nullable = false)
    private String slackMessage;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @Column(name = "slack_message_sender_id", length = 50)
    private String slackMessageSenderId;

    @Builder
    private SlackMessageEntity(
        UUID slackMessageId
        , String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTime
        , String slackMessageSenderId
    ) {
        this.slackMessageId = slackMessageId;
        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.sentTime = sentTime;
        this.slackMessageSenderId = slackMessageSenderId;
    }

    public static SlackMessageEntity create(
        UUID slackMessageId, String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTime
        , String slackMessageSenderId
    ) {
        return SlackMessageEntity.builder()
            .slackMessageId(slackMessageId)
            .receiveSlackId(receiveSlackId)
            .receiveUserId(receiveUserId)
            .slackMessageStatus(slackMessageStatus)
            .slackMessage(slackMessage)
            .sentTime(sentTime)
            .slackMessageSenderId(slackMessageSenderId)
            .build();
    }

    public void updateSlackMessage(
        String receiveSlackId
        , UUID receiveUserId
        , SlackMessageStatus slackMessageStatus
        , String slackMessage
        , LocalDateTime sentTime
        , String slackMessageSenderId
    ) {
        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.sentTime = sentTime;
        this.slackMessageSenderId = slackMessageSenderId;
    }

    public void changeSlackMessageStatus(SlackMessageStatus status) {
        this.slackMessageStatus = status;
    }
}
