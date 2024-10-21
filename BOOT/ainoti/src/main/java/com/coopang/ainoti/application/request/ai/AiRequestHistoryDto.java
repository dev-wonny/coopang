package com.coopang.ainoti.application.request.ai;

import com.coopang.apidata.application.ai.AiCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AiRequestHistoryDto {
    private UUID aiRequestHistoryId;
    private AiCategory aiCategory;
    private String aiRequest;
    private String aiResponse;

    @Builder
    private AiRequestHistoryDto(
        UUID aiRequestHistoryId,
        AiCategory aiCategory,
        String aiRequest,
        String aiResponse
    ) {
        this.aiRequestHistoryId = aiRequestHistoryId;
        this.aiCategory = aiCategory;
        this.aiRequest = aiRequest;
        this.aiResponse = aiResponse;
    }

    public static AiRequestHistoryDto of(
        UUID aiRequestHistoryId,
        AiCategory aiCategory,
        String aiRequest,
        String aiResponse
    ) {
        return AiRequestHistoryDto.builder()
            .aiRequestHistoryId(aiRequestHistoryId)
            .aiCategory(aiCategory)
            .aiRequest(aiRequest)
            .aiResponse(aiResponse)
            .build();
    }

    @Override
    public String toString() {
        return "AiRequestHistoryDto{" +
            "aiRequestHistoryId=" + aiRequestHistoryId +
            ", aiCategory=" + aiCategory +
            ", aiRequest='" + aiRequest + '\'' +
            ", aiResponse='" + aiResponse + '\'' +
            '}';
    }

    public void createId(UUID aiRequestHistoryId) {
        this.aiRequestHistoryId = aiRequestHistoryId;
    }
}