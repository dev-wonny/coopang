package com.coopang.hub.application.request.hub;

import lombok.Data;

import java.util.UUID;

@Data
public class HubSearchCondition {
    private UUID hubId;
    private String hubName;//starsWith
    private UUID hubManagerId;
    private Boolean isDeleted = false;
}