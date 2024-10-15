package com.coopang.hub.presentation.request;

import lombok.Data;

import java.util.UUID;

@Data
public class HubSearchCondition {
    private String hubName;
    private UUID hubManagerId;
}
