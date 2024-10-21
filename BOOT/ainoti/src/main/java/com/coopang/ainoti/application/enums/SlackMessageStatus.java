package com.coopang.ainoti.application.enums;

public enum SlackMessageStatus {
    READY, SUCCESS, FAIL;

    public static SlackMessageStatus getStatusEnum(String s) {
        try {
            return SlackMessageStatus.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Slack Message status: " + s);
        }
    }
}

