package com.coopang.ainoti.application.response.ai;


import com.coopang.ainoti.domain.entity.ai.AiRequestHistoryEntity;
import com.coopang.apidata.application.ai.AiCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class AiRequestHistoryResponseDto {
    private UUID aiRequestHistoryId;
    private AiCategory aiCategory;
    private String aiRequest;
    private String aiResponse;
    private boolean isDeleted;

    @Builder
    private AiRequestHistoryResponseDto(
        UUID aiRequestHistoryId,
        AiCategory aiCategory,
        String aiRequest,
        String aiResponse,
        boolean isDeleted
    ) {
        this.aiRequestHistoryId = aiRequestHistoryId;
        this.aiCategory = aiCategory;
        this.aiRequest = aiRequest;
        this.aiResponse = aiResponse;
        this.isDeleted = isDeleted;
    }

    /**
     * 오로지 하나의 객체만 반환하도록 하여 객체를 재사용해 메모리를 아끼도록 유도
     *
     * @param aiRequestHistory AI 요청 기록 엔티티
     * @return AiRequestHistoryResponseDto
     */
    public static AiRequestHistoryResponseDto fromAiRequestHistory(AiRequestHistoryEntity aiRequestHistory) {
        return AiRequestHistoryResponseDto.builder()
            .aiRequestHistoryId(aiRequestHistory.getAiRequestHistoryId())
            .aiCategory(aiRequestHistory.getAiCategory())
            .aiRequest(aiRequestHistory.getAiRequest())
            .aiResponse(aiRequestHistory.getAiResponse())
            .isDeleted(aiRequestHistory.isDeleted())
            .build();
    }
}