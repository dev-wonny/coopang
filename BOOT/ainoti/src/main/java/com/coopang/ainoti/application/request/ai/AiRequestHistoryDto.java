package com.coopang.ainoti.application.request.ai;

import com.coopang.apidata.application.ai.AiCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AiRequestHistoryDto {
    private UUID aiRequestHistoryId;
    private AiCategory aiCategory;
    private String aiRequest;
    private String aiResponse;
}