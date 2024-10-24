package com.coopang.apidata.application.ai.response;

import com.coopang.apidata.application.ai.AiCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestHistoryResponse {
    private UUID aiRequestHistoryId;
    private AiCategory aiCategory;
    private String aiRequest;
    private String aiResponse;
    private boolean isDeleted;
}
