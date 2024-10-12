package com.coopang.ainoti.domain.entitiy.slackMessage;

import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.hc.core5.http.Message;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "p_slack_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class SlackMessageEntity extends BaseEntity {

    @Id
    @Column(name = "slack_message_id",nullable = false)
    public UUID slackMessageId;

    @Column(name = "receive_slack_id")
    public String receiveSlackId;

    @Column(name = "receive_user_id",nullable = false)
    public UUID receiveUserId;

    @Column(name = "slack_message_status",nullable = false)
    @Enumerated(EnumType.STRING)
    public SlackMessageStatus slackMessageStatus;

    @Column(name = "slack_message")
    public String slackMessage;

    @Column(name = "sent_time")
    public LocalDateTime sentTime;

    @Column(name = "slack_message_sender_id")
    public String slackMessageSenderId;

    @Builder(access = AccessLevel.PRIVATE)
    public SlackMessageEntity(UUID slackMessageId,String receiveSlackId,
        UUID receiveUserId,SlackMessageStatus slackMessageStatus,String slackMessage,
        LocalDateTime sentTime,String slackMessageSenderId)
    {
        this.slackMessageId = slackMessageId;
        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.sentTime = sentTime;
        this.slackMessageSenderId = slackMessageSenderId;
    }


    public static SlackMessageEntity create(UUID slackMessageId,String receiveSlackId,
        UUID receiveUserId,SlackMessageStatus slackMessageStatus,String slackMessage,String slackMessageSenderId) {

        return SlackMessageEntity.builder()
            .slackMessageId(slackMessageId == null ? UUID.randomUUID() : slackMessageId)
            .receiveSlackId(receiveSlackId)
            .receiveUserId(receiveUserId)
            .slackMessageStatus(slackMessageStatus)
            .slackMessage(slackMessage)
            .sentTime(LocalDateTime.now())
            .slackMessageSenderId(slackMessageSenderId)
            .build();
    }

    public void update(String receiveSlackId,
        UUID receiveUserId,SlackMessageStatus slackMessageStatus,String slackMessage,String slackMessageSenderId) {

        this.receiveSlackId = receiveSlackId;
        this.receiveUserId = receiveUserId;
        this.slackMessageStatus = slackMessageStatus;
        this.slackMessage = slackMessage;
        this.slackMessageSenderId = slackMessageSenderId;

    }
}
