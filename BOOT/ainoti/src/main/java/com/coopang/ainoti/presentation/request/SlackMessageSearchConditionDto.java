package com.coopang.ainoti.presentation.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class SlackMessageSearchConditionDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UUID receiveUserId;
    private String message;
}
