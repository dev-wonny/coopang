package com.coopang.hub.application.request.hub;

import lombok.Data;

import java.util.UUID;

@Data
public class HubDto {
    private UUID hubId;
    private String hubName;
    private UUID hubManagerId;

    private String zipCode;
    private String address1;
    private String address2;
}