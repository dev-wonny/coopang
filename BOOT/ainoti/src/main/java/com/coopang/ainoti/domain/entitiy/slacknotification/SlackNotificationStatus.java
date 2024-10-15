package com.coopang.ainoti.domain.entitiy.slacknotification;

import lombok.Getter;

@Getter
public enum SlackNotificationStatus {
    READY("발송 대기"),
    SUCCESS("성공"),
    FAIL("실패"),
    ;

    private final String description;

    SlackNotificationStatus(String description) {
        this.description = description;
    }
}
