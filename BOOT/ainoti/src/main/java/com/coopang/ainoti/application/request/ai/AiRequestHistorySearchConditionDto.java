package com.coopang.ainoti.application.request.ai;

import com.coopang.apidata.application.ai.AiCategory;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
public class AiRequestHistorySearchConditionDto {
    private UUID aiRequestHistoryId;  // AI 요청 기록 ID
    private AiCategory aiCategory;    // AI 카테고리
    private String aiRequest;         // AI 요청 메시지 (contains)
    private boolean isDeleted;  // 삭제 여부

    @Builder
    private AiRequestHistorySearchConditionDto(
        UUID aiRequestHistoryId
        , AiCategory aiCategory
        , String aiRequest
        , boolean isDeleted
    ) {
        this.aiRequestHistoryId = aiRequestHistoryId;
        this.aiCategory = aiCategory;
        this.aiRequest = aiRequest;
        this.isDeleted = isDeleted;
    }

    public static AiRequestHistorySearchConditionDto empty() {
        return AiRequestHistorySearchConditionDto.builder()
            .aiRequestHistoryId(null)
            .aiCategory(null)
            .aiRequest(null)
            .isDeleted(false)
            .build();
    }

    public static AiRequestHistorySearchConditionDto from(
        UUID aiRequestHistoryId
        , String aiCategory
        , String aiRequest
        , boolean isDeleted
    ) {
        return AiRequestHistorySearchConditionDto.builder()
            .aiRequestHistoryId(aiRequestHistoryId)
            .aiCategory(!StringUtils.isBlank(aiCategory) ? AiCategory.getEnum(aiCategory) : null)
            .aiRequest(aiRequest)
            .isDeleted(!ObjectUtils.isEmpty(isDeleted) && isDeleted)
            .build();
    }

    public void setIsDeletedFalse() {
        this.isDeleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AiRequestHistorySearchConditionDto that = (AiRequestHistorySearchConditionDto) o;
            return
                Objects.equals(this.aiRequestHistoryId, that.aiRequestHistoryId)
                    && this.aiCategory == that.aiCategory
                    && Objects.equals(this.aiRequest, that.aiRequest)
                    && this.isDeleted == that.isDeleted;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.aiRequestHistoryId, this.aiCategory, this.aiRequest, this.isDeleted);
    }

    @Override
    public String toString() {
        return
            "AiRequestHistorySearchConditionDto(aiRequestHistoryId=" + this.aiRequestHistoryId
                + ", aiCategory=" + this.aiCategory
                + ", aiRequest=" + this.aiRequest
                + ", isDeleted=" + this.isDeleted
                + ")";
    }
}
