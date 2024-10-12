package com.coopang.ainoti.domain.entitiy.slackMessage;

import lombok.Getter;

@Getter
public enum SlackMessageStatus {
    READY("발송 대기"),
    SUCCESS("성공"),
    FAIL("실패"),
    ;

    private final String description;

    SlackMessageStatus(String description) {
        this.description = description;
    }
}
