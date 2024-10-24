package com.coopang.apidata.application.ai.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAiRequestHistoryRequest {
    private String aiCategory;// 선택적 필드

    @NotBlank(message = "AI 요청 메시지는 필수입니다.")
    private String aiRequest;

    private String aiResponse;// 선택적 필드
}
