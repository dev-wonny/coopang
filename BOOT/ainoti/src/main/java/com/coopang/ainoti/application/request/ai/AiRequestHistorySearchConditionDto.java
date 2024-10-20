package com.coopang.ainoti.application.request.ai;

import com.coopang.apidata.application.ai.AiCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AiRequestHistorySearchConditionDto {
    private UUID aiRequestHistoryId;  // AI 요청 기록 ID
    private AiCategory aiCategory;    // AI 카테고리
    private String aiRequest;         // AI 요청 메시지 (contains)
    private boolean isDeleted = false;  // 삭제 여부
}
