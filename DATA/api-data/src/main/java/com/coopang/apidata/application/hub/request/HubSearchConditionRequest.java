package com.coopang.apidata.application.hub.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HubSearchConditionRequest {
    private UUID hubId;
    private String hubName;//starsWith
    private UUID hubManagerId;
    private Boolean isDeleted;
}
