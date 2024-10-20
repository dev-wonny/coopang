package com.coopang.hub.presentation.request.hub;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HubSearchConditionRequestDto {
    private UUID hubId;
    private String hubName;//starsWith
    private UUID hubManagerId;
    private boolean isDeleted = false;
}
