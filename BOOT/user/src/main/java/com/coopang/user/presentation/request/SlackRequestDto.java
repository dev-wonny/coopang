package com.coopang.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SlackRequestDto {
    @NotBlank(message = "slack id is required.")
    private String slackId;
}
