package com.coopang.ainoti.presentation.request.ai;

import com.coopang.apidata.application.ai.AiCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAiRequestHistoryRequestDto {

    @NotNull(message = "AI 카테고리는 필수입니다.")
    private AiCategory aiCategory;

    @NotBlank(message = "AI 요청 메시지는 필수입니다.")
    private String aiRequest;

    @NotBlank(message = "AI 응답 메시지는 필수입니다.")
    private String aiResponse;
}