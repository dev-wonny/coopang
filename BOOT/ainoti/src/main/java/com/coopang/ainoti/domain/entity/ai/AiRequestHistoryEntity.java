package com.coopang.ainoti.domain.entity.ai;

import com.coopang.apidata.application.ai.AiCategory;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_ai_request_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class AiRequestHistoryEntity extends BaseEntity {

    @Id
    @Column(name = "ai_request_history_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID aiRequestHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_category", nullable = false)
    private AiCategory aiCategory;

    @Column(name = "ai_request", columnDefinition = "TEXT", nullable = false)
    private String aiRequest;

    @Column(name = "ai_response", columnDefinition = "TEXT", nullable = false)
    private String aiResponse;

    @Builder
    private AiRequestHistoryEntity(UUID aiRequestHistoryId, AiCategory aiCategory, String aiRequest, String aiResponse) {
        this.aiRequestHistoryId = aiRequestHistoryId;
        this.aiCategory = aiCategory;
        this.aiRequest = aiRequest;
        this.aiResponse = aiResponse;
    }

    /**
     * 정적 팩토리 메서드 (create 메서드)
     */
    public static AiRequestHistoryEntity create(
        UUID aiRequestHistoryId
        , AiCategory aiCategory
        , String aiRequest
        , String aiResponse
    ) {
        return AiRequestHistoryEntity.builder()
            .aiRequestHistoryId(aiRequestHistoryId)
            .aiCategory(aiCategory)
            .aiRequest(aiRequest)
            .aiResponse(aiResponse)
            .build();
    }

    /**
     * AI 요청 및 응답 업데이트
     */
    public void update(
        AiCategory aiCategory
        , String aiRequest
        , String aiResponse
    ) {
        this.aiCategory = aiCategory;
        this.aiRequest = aiRequest;
        this.aiResponse = aiResponse;
    }
}
